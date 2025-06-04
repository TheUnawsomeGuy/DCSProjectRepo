package Consistency;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A replica node that stores data and can apply operations
 * Supports different consistency models
 */
public class ReplicaNode {
    private final String nodeId;
    private final Map<String, String> dataStore;
    private int lastAppliedSequence;
    private final Random random;

    public ReplicaNode(String nodeId) {
        this.nodeId = nodeId;
        this.dataStore = new ConcurrentHashMap<>();
        this.lastAppliedSequence = 0;
        this.random = new Random();
    }

    /**
     * Apply an operation to this replica
     * Used for sequential consistency
     */
    public boolean applyOperation(Operation operation) {
        try {
            // Simulate network delay for eventual consistency scenarios
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
     * Local read operation
     */
    public String get(String key) {
        String value = dataStore.get(key);
        System.out.println(nodeId + ": Read " + key + " = " + value + " (seq: " + lastAppliedSequence + ")");
        return value;
    }

  
} 