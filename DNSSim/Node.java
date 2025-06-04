package DNSSim;
import java.util.Optional;

/*
 * This class represents a node in the network.
 * It can use a central DNS or its own local flat name service.
 * It can also find a resource by resolving its name via DNS or performing a local lookup.
 * It can also register a resource locally.
 */

public class Node {
    private String nodeId;
    private DNSSimulator dnsSimulator; // Each node might use a shared DNS
    private FlatNamingService localFlatNameService; // Or a local naming service

    // Constructor for using a central DNS
    public Node(String nodeId, DNSSimulator dnsSimulator) {
        this.nodeId = nodeId;
        this.dnsSimulator = dnsSimulator;
        this.localFlatNameService = null; // Not used in this configuration
    }

    // Constructor for using its own local flat name service
    public Node(String nodeId) {
        this.nodeId = nodeId;
        this.localFlatNameService = new FlatNamingService();
        this.dnsSimulator = null; // Not used in this configuration
    }

    // Node attempts to find where a resource is (e.g. by resolving its name via DNS)
    public Optional<String> findResourceLocation(String resourceDnsName) {
        if (dnsSimulator != null) {
            System.out.println("Node " + nodeId + " resolving '" + resourceDnsName + "' via DNS.");
            return dnsSimulator.lookup(resourceDnsName);
        } else {
            System.out.println("Node " + nodeId + " does not have DNS access for '" + resourceDnsName + "'.");
            return Optional.empty();
        }
    }

    // Node attempts to find a resource using its local flat name service
    public Optional<String> lookupLocally(String flatName) {
        if (localFlatNameService != null) {
            System.out.println("Node " + nodeId + " performing local lookup for '" + flatName + "'.");
            return localFlatNameService.lookup(flatName);
        } else {
            System.out.println("Node " + nodeId + " does not have a local flat name service.");
            return Optional.empty();
        }
    }

    public void registerResourceLocally(String flatName, Resource resource){
        if(localFlatNameService != null){
            localFlatNameService.register(flatName, resource);
        } else {
            System.out.println("Node " + nodeId + " cannot register locally: no local service.");
        }
    }


    public String getNodeId() {
        return nodeId;
    }
}