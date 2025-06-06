package Final;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Central coordinator for managing sequential consistency
 * Maintains a global log of operations and assigns sequence numbers
 */
public class CentralCoordinator {
    private final List<Operation> operationLog;
    private final AtomicInteger sequenceCounter;
    private final Queue<Operation> pendingOperations;

    public CentralCoordinator() {
        this.operationLog = Collections.synchronizedList(new ArrayList<>());
        this.sequenceCounter = new AtomicInteger(0);
        this.pendingOperations = new ConcurrentLinkedQueue<>();
    }

    /**
     * Submit an operation to the coordinator
     * Returns the operation with assigned sequence number
     */
    public Operation submitOperation(String type, String key, String value, String clientId) {
        int sequenceNumber = sequenceCounter.incrementAndGet();
        Operation operation = new Operation(sequenceNumber, type, key, value, clientId);
        
        synchronized (operationLog) {
            operationLog.add(operation);
            pendingOperations.offer(operation);
        }
        
        System.out.println("Coordinator: Received " + operation);
        return operation;
    }

    /**
     * Get operations that happened after a specific sequence number
     * Used for eventual consistency synchronization
     */
    public List<Operation> getOperationsSince(int lastSequence) {
        List<Operation> result = new ArrayList<>();
        
        synchronized (operationLog) {
            for (Operation op : operationLog) {
                if (op.getSequenceNumber() > lastSequence) {
                    result.add(op);
                }
            }
        }
        
        return result;
    }

    /**
     * Get pending operations for sequential consistency
     */
    public List<Operation> getPendingOperations() {
        List<Operation> result = new ArrayList<>();
        Operation op;
        while ((op = pendingOperations.poll()) != null) {
            result.add(op);
        }
        return result;
    }

    /**
     * Get all operations in the log
     */
    public List<Operation> getAllOperations() {
        synchronized (operationLog) {
            return new ArrayList<>(operationLog);
        }
    }

    /**
     * Get the total number of operations
     */
    public int getTotalOperations() {
        return operationLog.size();
    }

    /**
     * Get the current sequence number
     */
    public int getCurrentSequence() {
        return sequenceCounter.get();
    }

    /**
     * Print the operation log
     */
    public void printLog() {
        System.out.println("Central Coordinator Log (" + operationLog.size() + " operations):");
        synchronized (operationLog) {
            for (Operation op : operationLog) {
                System.out.println("  " + op);
            }
        }
    }

    /**
     * Clear the operation log (for testing purposes)
     */
    public void clearLog() {
        synchronized (operationLog) {
            operationLog.clear();
            pendingOperations.clear();
        }
        sequenceCounter.set(0);
    }
} 