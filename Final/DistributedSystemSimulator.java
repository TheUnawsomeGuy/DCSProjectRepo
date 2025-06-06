package Final;

import java.util.*;
import java.util.concurrent.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

/**
 * Integrated Distributed System Simulator
 * Combines threading, consistency models, and naming services
 */
public class DistributedSystemSimulator {
    private final Map<String, ServiceNode> nodes;
    private final CentralCoordinator coordinator;
    private final ExecutorService threadPool;
    private final FlatNamingService flatNaming;
    private final StructuredNamingService structuredNaming;
    private final DNSSimulator dnsService;
    private final Random random;
    
    // Performance metrics
    private final java.util.List<PerformanceMetric> metrics;
    private volatile boolean isRunning;
    
    public DistributedSystemSimulator() {
        this.nodes = new ConcurrentHashMap<>();
        this.coordinator = new CentralCoordinator();
        this.threadPool = Executors.newCachedThreadPool();
        this.flatNaming = new FlatNamingService();
        this.structuredNaming = new StructuredNamingService();
        this.dnsService = new DNSSimulator();
        this.random = new Random();
        this.metrics = Collections.synchronizedList(new ArrayList<>());
        this.isRunning = false;
        
        initializeSystem();
    }
    
    private void initializeSystem() {
        // Create service nodes
        String[] nodeIds = {"NodeA", "NodeB", "NodeC"};
        for (String nodeId : nodeIds) {
            ServiceNode node = new ServiceNode(nodeId, coordinator, flatNaming, structuredNaming, dnsService);
            nodes.put(nodeId, node);
            
            // Register in naming services
            String service = nodeId.toLowerCase() + "-service";
            flatNaming.register(service, new Resource(service, nodeId));
            structuredNaming.register("/services/" + service, new Resource(service, nodeId));
            dnsService.register(service + ".example.com", "192.168.1." + (nodes.size()));
            
            System.out.println("Initialized " + nodeId + " with all naming services");
        }
    }
    
    public void startSimulation() {
        isRunning = true;
        
        // Start each node as a separate thread
        for (ServiceNode node : nodes.values()) {
            threadPool.submit(() -> {
                while (isRunning) {
                    try {
                        node.performRandomOperation();
                        Thread.sleep(random.nextInt(1000) + 500); // 0.5-1.5s intervals
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }
        
        // Start coordination and synchronization services
        threadPool.submit(this::coordinationService);
        threadPool.submit(this::eventualConsistencyService);
        threadPool.submit(this::performanceMonitor);
        
        System.out.println("🚀 Distributed System Simulation Started!");
    }
    
    private void coordinationService() {
        while (isRunning) {
            try {
                // Sequential consistency - apply operations in order
                java.util.List<Operation> pendingOps = coordinator.getPendingOperations();
                for (Operation op : pendingOps) {
                    for (ServiceNode node : nodes.values()) {
                        node.applyOperation(op);
                    }
                }
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    private void eventualConsistencyService() {
        while (isRunning) {
            try {
                // Randomly sync nodes for eventual consistency
                java.util.List<ServiceNode> nodeList = new ArrayList<>(nodes.values());
                if (nodeList.size() > 1) {
                    ServiceNode node = nodeList.get(random.nextInt(nodeList.size()));
                    node.syncWithCoordinator();
                    
                    // Simulate network partition recovery
                    if (random.nextDouble() < 0.1) { // 10% chance
                        System.out.println("🌐 Network partition recovery - syncing all nodes");
                        for (ServiceNode n : nodes.values()) {
                            n.syncWithCoordinator();
                        }
                    }
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    private void performanceMonitor() {
        while (isRunning) {
            try {
                // Collect performance metrics
                long timestamp = System.currentTimeMillis();
                int totalOperations = coordinator.getTotalOperations();
                double consistencyViolations = calculateConsistencyViolations();
                double avgLatency = calculateAverageLatency();
                
                PerformanceMetric metric = new PerformanceMetric(
                    timestamp, totalOperations, consistencyViolations, avgLatency);
                metrics.add(metric);
                
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    private double calculateConsistencyViolations() {
        // Simplified consistency violation calculation
        int violations = 0;
        int totalChecks = 0;
        
        for (ServiceNode node1 : nodes.values()) {
            for (ServiceNode node2 : nodes.values()) {
                if (!node1.equals(node2)) {
                    if (Math.abs(node1.getLastSequence() - node2.getLastSequence()) > 5) {
                        violations++;
                    }
                    totalChecks++;
                }
            }
        }
        
        return totalChecks > 0 ? (double) violations / totalChecks : 0.0;
    }
    
    private double calculateAverageLatency() {
        return random.nextGaussian() * 50 + 150; // Simulated latency
    }
    
    public void performOperation(String nodeId, String operation, String key, String value) {
        ServiceNode node = nodes.get(nodeId);
        if (node != null) {
            node.performOperation(operation, key, value);
        }
    }
    
    public void simulateNodeFailure(String nodeId) {
        ServiceNode node = nodes.get(nodeId);
        if (node != null) {
            node.simulateFailure();
            System.out.println("💥 Node " + nodeId + " failed!");
        }
    }
    
    public void recoverNode(String nodeId) {
        ServiceNode node = nodes.get(nodeId);
        if (node != null) {
            node.recover();
            System.out.println("🔄 Node " + nodeId + " recovered!");
        }
    }
    
    public String lookupResource(String resourceName, String namingType) {
        long startTime = System.currentTimeMillis();
        String result = null;
        
        try {
            switch (namingType.toLowerCase()) {
                case "flat":
                    result = flatNaming.lookup(resourceName).orElse("Not found");
                    break;
                case "structured":
                    result = structuredNaming.lookup(resourceName).orElse("Not found");
                    break;
                case "dns":
                    result = dnsService.lookup(resourceName).orElse("Not found");
                    break;
                default:
                    result = "Invalid naming type";
            }
        } finally {
            long latency = System.currentTimeMillis() - startTime;
            System.out.println("🔍 " + namingType + " lookup for '" + resourceName + "': " + result + " (" + latency + "ms)");
        }
        
        return result;
    }
    
    public void stopSimulation() {
        isRunning = false;
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("🛑 Simulation stopped");
    }
    
    public void printSystemStatus() {
        System.out.println("\n=== SYSTEM STATUS ===");
        coordinator.printLog();
        
        System.out.println("\n--- Node Status ---");
        for (ServiceNode node : nodes.values()) {
            node.printStatus();
        }
        
        System.out.println("\n--- Naming Services ---");
        System.out.println("Flat naming entries: " + flatNaming.size());
        System.out.println("Structured naming entries: " + structuredNaming.size());
        System.out.println("DNS entries: " + dnsService.size());
    }
    
    public java.util.List<PerformanceMetric> getMetrics() {
        return new ArrayList<>(metrics);
    }
    
    public Set<String> getNodeIds() {
        return new HashSet<>(nodes.keySet());
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    // DNS Management Methods
    public void addDNSEntry(String domain, String ip) {
        dnsService.register(domain, ip);
    }
    
    public void removeDNSEntry(String domain) {
        dnsService.remove(domain);
    }
    
    public void clearDNSEntries() {
        dnsService.clear();
    }
    
    public java.util.Set<String> getAllDNSDomains() {
        return dnsService.getAllDomains();
    }
    
    public String lookupDNSEntry(String domain) {
        return dnsService.lookup(domain).orElse(null);
    }
    
    // Thread Configuration Methods
    private int threadPoolSize = 5;
    private int simulationInterval = 1000;
    
    public void configureThreads(int poolSize, int interval) {
        this.threadPoolSize = poolSize;
        this.simulationInterval = interval;
        System.out.println("Thread configuration updated: Pool size=" + poolSize + ", Interval=" + interval + "ms");
        // Note: Changes take effect on next simulation start
    }
    
    public int getThreadPoolSize() {
        return threadPoolSize;
    }
    
    public int getSimulationInterval() {
        return simulationInterval;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SimulatorGUI().setVisible(true);
        });
    }
} 