package Final;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service node that represents a distributed system participant
 * Combines threading, data storage, and consistency models
 */
public class ServiceNode {
    private final String nodeId;
    private final Map<String, String> dataStore;
    private final CentralCoordinator coordinator;
    private final FlatNamingService flatNaming;
    private final StructuredNamingService structuredNaming;
    private final DNSSimulator dnsService;
    private final Random random;
    
    // Client-centric consistency tracking
    private final Map<String, Integer> clientVersions;
    private int lastAppliedSequence;
    private boolean isAvailable;
    
    // Threading and resource management
    private int sharedResource;
    private final Object resourceLock;

    public ServiceNode(String nodeId, CentralCoordinator coordinator, 
                      FlatNamingService flatNaming, StructuredNamingService structuredNaming,
                      DNSSimulator dnsService) {
        this.nodeId = nodeId;
        this.coordinator = coordinator;
        this.flatNaming = flatNaming;
        this.structuredNaming = structuredNaming;
        this.dnsService = dnsService;
        this.dataStore = new ConcurrentHashMap<>();
        this.clientVersions = new ConcurrentHashMap<>();
        this.random = new Random();
        this.lastAppliedSequence = 0;
        this.isAvailable = true;
        this.sharedResource = 100; // Initial resource value for threading demo
        this.resourceLock = new Object();
    }

    /**
     * Perform a random operation (used in simulation)
     */
    public void performRandomOperation() {
        if (!isAvailable) return;
        
        String[] operations = {"PUT", "GET", "DELETE"};
        String operation = operations[random.nextInt(operations.length)];
        String key = "key" + random.nextInt(10);
        String value = "value" + random.nextInt(100);
        
        performOperation(operation, key, value);
    }

    /**
     * Perform a specific operation
     */
    public void performOperation(String operation, String key, String value) {
        if (!isAvailable) {
            System.out.println(nodeId + ": Node unavailable for operation " + operation);
            return;
        }

        String clientId = nodeId + "-client";
        
        switch (operation.toUpperCase()) {
            case "PUT":
                Operation putOp = coordinator.submitOperation("PUT", key, value, clientId);
                applyOperation(putOp);
                updateClientVersion(clientId, putOp.getSequenceNumber());
                break;
                
            case "GET":
                String retrievedValue = get(key);
                System.out.println(nodeId + ": GET " + key + " = " + retrievedValue);
                break;
                
            case "DELETE":
                Operation deleteOp = coordinator.submitOperation("DELETE", key, null, clientId);
                applyOperation(deleteOp);
                updateClientVersion(clientId, deleteOp.getSequenceNumber());
                break;
                
            case "DEPOSIT":
                performDeposit();
                break;
                
            case "WITHDRAW":
                performWithdraw();
                break;
                
            default:
                System.out.println(nodeId + ": Unknown operation " + operation);
        }
    }

    /**
     * Apply an operation to this node (for consistency)
     */
    public boolean applyOperation(Operation operation) {
        if (!isAvailable) return false;
        
        try {
            // Simulate network delay for eventual consistency
            if (random.nextBoolean()) {
                Thread.sleep(random.nextInt(100) + 50); // 50-150ms delay
            }
            
            switch (operation.getType()) {
                case "PUT":
                    dataStore.put(operation.getKey(), operation.getValue());
                    break;
                case "DELETE":
                    dataStore.remove(operation.getKey());
                    break;
                default:
                    return false;
            }
            
            lastAppliedSequence = operation.getSequenceNumber();
            System.out.println(nodeId + ": Applied " + operation);
            return true;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * Perform deposit operation (from threading demo)
     */
    public void performDeposit() {
        synchronized (resourceLock) {
            int oldValue = sharedResource;
            try {
                Thread.sleep(100); // Simulate processing time
                sharedResource += 10;
                System.out.println("[" + nodeId + "] Deposit: " + oldValue + " + 10 = " + sharedResource);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Perform withdraw operation (from threading demo)
     */
    public void performWithdraw() {
        synchronized (resourceLock) {
            int oldValue = sharedResource;
            if (sharedResource >= 6) {
                try {
                    Thread.sleep(150); // Simulate processing time
                    sharedResource -= 6;
                    System.out.println("[" + nodeId + "] Withdraw: " + oldValue + " - 6 = " + sharedResource);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                System.out.println("[" + nodeId + "] Withdraw failed: Insufficient funds (" + sharedResource + ")");
            }
        }
    }

    /**
     * Get value with client-centric consistency
     */
    public String get(String key) {
        String value = dataStore.get(key);
        System.out.println(nodeId + ": Read " + key + " = " + value + " (seq: " + lastAppliedSequence + ")");
        return value;
    }

    /**
     * Update client version for client-centric consistency
     */
    private void updateClientVersion(String clientId, int version) {
        clientVersions.put(clientId, version);
    }

    /**
     * Check if this node can serve a client based on their last seen version
     */
    public boolean canServeClient(String clientId, int clientVersion) {
        return lastAppliedSequence >= clientVersion;
    }

    /**
     * Sync with coordinator for eventual consistency
     */
    public void syncWithCoordinator() {
        if (!isAvailable) return;
        
        List<Operation> missedOperations = coordinator.getOperationsSince(lastAppliedSequence);
        
        if (!missedOperations.isEmpty()) {
            System.out.println(nodeId + ": Syncing " + missedOperations.size() + " missed operations");
            for (Operation op : missedOperations) {
                applyOperation(op);
            }
        }
    }

    /**
     * Lookup resource using different naming services
     */
    public String lookupResource(String resourceName, String namingType) {
        try {
            switch (namingType.toLowerCase()) {
                case "flat":
                    return flatNaming.lookup(resourceName).orElse("Not found");
                case "structured":
                    return structuredNaming.lookup(resourceName).orElse("Not found");
                case "dns":
                    return dnsService.lookup(resourceName).orElse("Not found");
                default:
                    return "Invalid naming type";
            }
        } catch (Exception e) {
            return "Lookup failed: " + e.getMessage();
        }
    }

    /**
     * Simulate node failure
     */
    public void simulateFailure() {
        isAvailable = false;
        System.out.println(nodeId + ": Node failed!");
    }

    /**
     * Recover from failure
     */
    public void recover() {
        isAvailable = true;
        syncWithCoordinator(); // Catch up on missed operations
        System.out.println(nodeId + ": Node recovered and synced!");
    }

    /**
     * Print node status
     */
    public void printStatus() {
        System.out.println(nodeId + " Status:");
        System.out.println("  Available: " + isAvailable);
        System.out.println("  Last sequence: " + lastAppliedSequence);
        System.out.println("  Shared resource: " + sharedResource);
        System.out.println("  Data store: " + dataStore);
        System.out.println("  Client versions: " + clientVersions);
    }

    // Getters
    public String getNodeId() { return nodeId; }
    public int getLastSequence() { return lastAppliedSequence; }
    public boolean isAvailable() { return isAvailable; }
    public int getSharedResource() { return sharedResource; }
    public Map<String, String> getDataStore() { return new HashMap<>(dataStore); }
} 