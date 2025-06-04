package Consistency;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue; // info : https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentLinkedQueue.html

/**
 * Central coordinator that maintains a global operation log
 * Ensures sequential consistency by ordering all operations
 */
public class CentralCoordinator {
    private final Queue<Operation> operationLog;
    private final List<ReplicaNode> replicaNodes;
    private int currentSequenceNumber;

    public CentralCoordinator() {
        this.operationLog = new ConcurrentLinkedQueue<>();
        this.replicaNodes = new ArrayList<>();
        this.currentSequenceNumber = 0;
    }

    /**
     * Register a replica node with the coordinator
     */
    public void registerReplica(ReplicaNode node) {
        replicaNodes.add(node);
        System.out.println("Registered replica: " + node.getNodeId());
    }

    /**
     * Process an operation and ensure sequential consistency
     * All nodes apply operations in the same order
     */
    public synchronized boolean processOperation(String type, String key, String value, String clientId) {
        // Create operation with sequence number
        Operation operation = new Operation(++currentSequenceNumber, type, key, value, clientId);
        operationLog.add(operation);
        
        System.out.println("Coordinator: Processing " + operation);
        
        // Apply to all replicas in the same order
        boolean success = true;
        for (ReplicaNode replica : replicaNodes) {
            if (!replica.applyOperation(operation)) {
                success = false;
            }
        }
        
        return success;
    }

    /**
     * Get operation history for synchronization
     */
    public List<Operation> getOperationsSince(int sequenceNumber) {
        return operationLog.stream()
                .filter(op -> op.getSequenceNumber() > sequenceNumber)
                .sorted(Comparator.comparingInt(Operation::getSequenceNumber))
                .toList();
    }

    public int getCurrentSequenceNumber() {
        return currentSequenceNumber;
    }

    public void printOperationLog() {
        System.out.println("\n=== Central Operation Log ===");
        operationLog.forEach(System.out::println);
        System.out.println("=============================\n");
    }
} 