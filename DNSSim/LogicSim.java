package DNSSim;
import java.util.Optional;

public class LogicSim {
    public static void main(String[] args) {
        System.out.println("--- Initializing Services ---");
        FlatNamingService globalFlatService = new FlatNamingService();
        StructuredNamingService globalStructuredService = new StructuredNamingService();
        DNSSimulator globalDns = new DNSSimulator();

        System.out.println("\n--- Registering Resources ---");
        Resource res1 = new Resource("res001", "nodeA_ip");
        Resource res2 = new Resource("res002", "nodeB_ip");
        Resource res3 = new Resource("res003", "nodeC_ip");

        // Flat Naming
        globalFlatService.register("web-server-alpha", res1);
        globalFlatService.register("database-main", res2);

        // Structured Naming
        globalStructuredService.register("/services/web/alpha", res1);
        globalStructuredService.register("/services/db/main", res2);
        globalStructuredService.register("/services/auth/primary", res3);

        // DNS Simulation
        globalDns.addRecord("web-alpha.example.com", "nodeA_ip"); // nodeA_ip is where res1 is
        globalDns.addRecord("db-main.example.com", "nodeB_ip");   // nodeB_ip is where res2 is
        globalDns.addRecord("auth-primary.example.com", "nodeC_ip"); // nodeC_ip is where res3 is


        System.out.println("\n--- Simulating Nodes and Lookups ---");
        Node nodeClient = new Node("clientNode", globalDns); // Client node uses the global DNS

        // Node A hosts the web server, it might register its resource in a local service (or just know about it)
        Node nodeA = new Node("nodeA_compute_unit");
        nodeA.registerResourceLocally("local-web-server", res1);


        // Scenario 1: Client wants to find web-alpha.example.com
        System.out.println("\nScenario 1: Client finding web-alpha.example.com");
        Optional<String> webServerLocation = nodeClient.findResourceLocation("web-alpha.example.com");
        if (webServerLocation.isPresent()) {
            System.out.println("Client found web server at: " + webServerLocation.get());
            // Now client could "connect" to nodeA_ip.
            // If nodeA_ip was an actual node object, it could then try to get the resource.
            if(webServerLocation.get().equals(res1.getLocation())){
                 System.out.println("Client successfully 'retrieved' " + res1.getId() + " from " + res1.getLocation());
            }
        }

        // Scenario 2: Direct lookup in flat name service (e.g., by an admin tool)
        System.out.println("\nScenario 2: Admin tool flat lookup for 'database-main'");
        globalFlatService.lookup("database-main");

        // Scenario 3: Direct lookup in structured name service
        System.out.println("\nScenario 3: Admin tool structured lookup for '/services/auth/primary'");
        Optional<String> authResource = globalStructuredService.lookup("/services/auth/primary");
        authResource.ifPresent(r -> System.out.println("Found auth resource: " + r));


        // Scenario 4: Node A doing a local lookup
        System.out.println("\nScenario 4: Node A local lookup");
        nodeA.lookupLocally("local-web-server");


        // Simulating resolution across nodes:
        // The DnsSimulator acts as the central point. A request from 'nodeClient' for "web-alpha.example.com"
        // goes to 'globalDns'. 'globalDns' returns "nodeA_ip".
        // The 'nodeClient' now "knows" the resource is on "nodeA_ip".
        // A more complex simulation would involve message passing between Node objects.
        // For instance, nodeClient, after getting "nodeA_ip", could send a "GET_RESOURCE" message
        // to a Node object representing "nodeA_ip".

        System.out.println("\n--- Simulation Complete ---");
    }
}