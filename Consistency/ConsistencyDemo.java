package Consistency;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates different consistency models:
 * - Sequential Consistency
 * - Eventual Consistency  
 * - Client-Centric Consistency
 * (Dev note: This code is a black box. Thanks to [List Contributors] for the code.)
 */
public class ConsistencyDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Distributed Consistency Models Demo ===\n");
        
        // Setup the system
        CentralCoordinator coordinator = new CentralCoordinator();
        
        // Create replica nodes
        ReplicaNode replica1 = new ReplicaNode("Replica-A");
        ReplicaNode replica2 = new ReplicaNode("Replica-B");
        ReplicaNode replica3 = new ReplicaNode("Replica-C");
        
        coordinator.registerReplica(replica1);
        coordinator.registerReplica(replica2);
        coordinator.registerReplica(replica3);
        
        // Create a mutable list instead of Arrays.asList()
        List<ReplicaNode> replicas = new ArrayList<>();
        replicas.add(replica1);
        replicas.add(replica2);
        replicas.add(replica3);
        
        // Create clients
        ConsistencyClient client1 = new ConsistencyClient("Client-1", coordinator, replicas);
        ConsistencyClient client2 = new ConsistencyClient("Client-2", coordinator, replicas);
        
        System.out.println("=== 1. SEQUENTIAL CONSISTENCY DEMO ===");
        demonstrateSequentialConsistency(coordinator, client1, client2, replicas);
        
        System.out.println("\n=== 2. EVENTUAL CONSISTENCY DEMO ===");
        demonstrateEventualConsistency(coordinator, replicas);
        
        System.out.println("\n=== 3. CLIENT-CENTRIC CONSISTENCY DEMO ===");
        demonstrateClientCentricConsistency(client1, client2, replicas);
        
        System.out.println("\n=== Demo Complete ===");
    }

    private static void demonstrateSequentialConsistency(CentralCoordinator coordinator, 
                                                        ConsistencyClient client1,
                                                        ConsistencyClient client2,
                                                        List<ReplicaNode> replicas) throws InterruptedException {
        System.out.println("All operations are applied in the same order across all replicas\n");
        
        // Multiple clients perform operations
        client1.write("x", "10");
        client2.write("y", "20");
        client1.write("x", "15");
        client2.write("z", "30");
        
        Thread.sleep(500); // Let all operations complete
        
        System.out.println("\nFinal state (should be identical across all replicas):");
        for (ReplicaNode replica : replicas) {
            replica.printDataStore();
        }
        
        coordinator.printOperationLog();
    }

    private static void demonstrateEventualConsistency(CentralCoordinator coordinator,
                                                      List<ReplicaNode> replicas) throws InterruptedException {
        System.out.println("Nodes may be temporarily inconsistent but eventually converge\n");
        
        EventualConsistencySimulator eventualSim = new EventualConsistencySimulator(coordinator, replicas);
        eventualSim.startEventualSync();
        
        // Perform some operations
        coordinator.processOperation("PUT", "eventual_key", "value1", "system");
        
        System.out.println("\nImmediate state (may be inconsistent):");
        for (ReplicaNode replica : replicas) {
            replica.printDataStore();
        }
        
        // Simulate partition
        eventualSim.simulatePartition(replicas.get(2), 3);
        
        // More operations during partition
        coordinator.processOperation("PUT", "eventual_key", "value2", "system");
        coordinator.processOperation("PUT", "partitioned_data", "test", "system");
        
        System.out.println("\nDuring partition:");
        for (ReplicaNode replica : replicas) {
            replica.printDataStore();
        }
        
        // Wait for eventual consistency
        Thread.sleep(5000);
        
        System.out.println("\nAfter eventual consistency:");
        for (ReplicaNode replica : replicas) {
            replica.printDataStore();
        }
        
        eventualSim.shutdown();
    }

    private static void demonstrateClientCentricConsistency(ConsistencyClient client1,
                                                           ConsistencyClient client2,
                                                           List<ReplicaNode> replicas) throws InterruptedException {
        System.out.println("Each client sees a consistent view based on their operations\n");
        
        // Client 1 writes and reads (read-your-writes)
        client1.write("user_pref", "dark_mode");
        String pref = client1.read("user_pref"); // Should see "dark_mode"
        System.out.println("Client 1 read their own write: " + pref);
        
        // Client 2 has different view initially
        String client2Pref = client2.read("user_pref");
        System.out.println("Client 2 read: " + client2Pref);
        
        Thread.sleep(1000);
        
        // Show version tracking
        client1.printVersionState();
        client2.printVersionState();
        
        // Client 2 now writes and should see consistent progression
        client2.write("user_pref", "light_mode");
        String newPref = client2.read("user_pref"); // Should see "light_mode"
        System.out.println("Client 2 read their own write: " + newPref);
        
        // Final state
        System.out.println("\nFinal replica states:");
        for (ReplicaNode replica : replicas) {
            replica.printDataStore();
        }
    }
} 