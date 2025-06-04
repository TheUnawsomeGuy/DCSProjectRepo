package Consistency;

import java.util.*;
import java.util.concurrent.*;

/**
 * Simulates eventual consistency with background synchronization
 */
public class EventualConsistencySimulator {
    private final CentralCoordinator coordinator;
    private final List<ReplicaNode> replicas;
    private final ScheduledExecutorService syncExecutor; // for background sync (dev note: ScheduledExecutorService is a thread pool that can schedule tasks to run at a later time. info : https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html)
    private final Random random;

    public EventualConsistencySimulator(CentralCoordinator coordinator, List<ReplicaNode> replicas) {
        this.coordinator = coordinator;
        this.replicas = replicas;
        this.syncExecutor = Executors.newScheduledThreadPool(2);
        this.random = new Random();
    }

    /**
     * Start background synchronization for eventual consistency
     */
    public void startEventualSync() {
        System.out.println("Starting eventual consistency background sync...");
        
        // Periodic sync every 2-5 seconds
        syncExecutor.scheduleWithFixedDelay(() -> {
            ReplicaNode replica = replicas.get(random.nextInt(replicas.size()));
            System.out.println("\n--- Background sync for " + replica.getNodeId() + " ---");
            replica.syncWithCoordinator(coordinator);
        }, 2, random.nextInt(3) + 2, TimeUnit.SECONDS);
    }

    /**
     * Simulate network partition - some nodes can't sync
     */
    public void simulatePartition(ReplicaNode partitionedNode, int durationSeconds) {
        System.out.println("\n!!! Simulating network partition for " + partitionedNode.getNodeId() + " !!!");
        
        // Remove from available replicas temporarily
        boolean wasAvailable = replicas.remove(partitionedNode);
        
        // Restore after duration
        syncExecutor.schedule(() -> {
            if (wasAvailable) {
                replicas.add(partitionedNode);
                System.out.println("\n!!! Partition healed for " + partitionedNode.getNodeId() + " - syncing !!!");
                partitionedNode.syncWithCoordinator(coordinator);
            }
        }, durationSeconds, TimeUnit.SECONDS);
    }

    public void shutdown() {
        syncExecutor.shutdown();
    }
} 