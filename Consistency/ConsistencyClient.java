package Consistency;

import java.util.*;

/**
 * Client that tracks versions for client-centric consistency
 * Implements read-your-writes and monotonic reads
 * (Dev note: Something interesting about client-centric consistency. It's quite hard to implement.
 *  More info : https://vitaminac.github.io/posts/Client-Centric-Consistency-Models/)
 */
public class ConsistencyClient {
    private final String clientId;
    private final Map<String, Integer> lastSeenVersions; // key -> sequence number
    private int lastWriteSequence;
    private final CentralCoordinator coordinator;
    private final List<ReplicaNode> availableReplicas;

    public ConsistencyClient(String clientId, CentralCoordinator coordinator, List<ReplicaNode> replicas) {
        this.clientId = clientId;
        this.coordinator = coordinator;
        this.availableReplicas = new ArrayList<>(replicas);
        this.lastSeenVersions = new HashMap<>();
        this.lastWriteSequence = 0;
    }

    /**
     * Write operation - ensures write consistency
     */
    public boolean write(String key, String value) {
        System.out.println("\n" + clientId + ": Writing " + key + " = " + value);
        
        boolean success = coordinator.processOperation("PUT", key, value, clientId);
        if (success) {
            lastWriteSequence = coordinator.getCurrentSequenceNumber();
            lastSeenVersions.put(key, lastWriteSequence);
            System.out.println(clientId + ": Write completed, version " + lastWriteSequence);
        }
        return success;
    }

    /**
     * Read operation with client-centric consistency
     * Ensures read-your-writes and monotonic reads
     */
    public String read(String key) {
        System.out.println("\n" + clientId + ": Reading " + key);
        
        // Find a replica that has at least our last written version
        ReplicaNode suitableReplica = findSuitableReplica(key);
        
        if (suitableReplica != null) {
            String value = suitableReplica.get(key);
            
            // Update our version tracking
            int currentVersion = suitableReplica.getLastAppliedSequence();
            lastSeenVersions.put(key, Math.max(lastSeenVersions.getOrDefault(key, 0), currentVersion));
            
            System.out.println(clientId + ": Read completed from " + suitableReplica.getNodeId());
            return value;
        }
        
        System.out.println(clientId + ": No suitable replica found for consistent read");
        return null;
    }

    /**
     * Find a replica that satisfies client-centric consistency requirements
     */
    private ReplicaNode findSuitableReplica(String key) {
        int requiredVersion = Math.max(
            lastSeenVersions.getOrDefault(key, 0),
            lastWriteSequence
        );
        
        for (ReplicaNode replica : availableReplicas) {
            if (replica.getLastAppliedSequence() >= requiredVersion) {
                return replica;
            }
        }
        
        // If no replica is up-to-date, sync one and use it
        if (!availableReplicas.isEmpty()) {
            ReplicaNode replica = availableReplicas.get(0);
            System.out.println(clientId + ": Syncing " + replica.getNodeId() + " for consistent read");
            replica.syncWithCoordinator(coordinator);
            if (replica.getLastAppliedSequence() >= requiredVersion) {
                return replica;
            }
        }
        
        return null;
    }

    public String getClientId() { return clientId; }
    
    public void printVersionState() {
        System.out.println(clientId + " versions: " + lastSeenVersions + ", last write: " + lastWriteSequence);
    }
} 