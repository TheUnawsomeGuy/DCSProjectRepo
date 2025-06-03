package DNSSim;

import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

public class DNSSimulator {
    
    private Map<String, String> dnsRecords;

    public DNSSimulator() {
        this.dnsRecords = new HashMap<>();
    }

    public void addRecord(String domainName, String ip) {
        dnsRecords.put(domainName.toLowerCase(), ip);
        System.out.println("Added DNS Record: " + domainName + " -> " + ip);
    }

    public Optional<String> lookup(String domainName) {
        String ip = dnsRecords.get(domainName.toLowerCase());
        if (ip == null) {
            System.out.println("DNS Lookup Failed: " + domainName);
            return Optional.empty();
        } else {
            System.out.println("DNS Lookup Successful: " + domainName + " -> " + ip);
            return Optional.of(ip);
        }
    }
    
    /*public static void main(String[] args) {
        System.out.println("DNS Simulator");
        System.out.println("It's Morbin' Time!");

        FlatNamingService flatNamingService = new FlatNamingService();
        StructuredNamingService structuredNamingService = new StructuredNamingService();

        flatNamingService.register("www.google.com", new Resource("www.google.com", "192.168.1.1"));
        structuredNamingService.register("www.google.com", new Resource("www.google.com", "192.168.1.1"));

        System.out.println(flatNamingService.lookup("www.google.com"));
        System.out.println(structuredNamingService.lookup("www.google.com"));
        System.out.println(structuredNamingService.lookup("www.Pojan.com"));

        flatNamingService.unregister("www.google.com");
        structuredNamingService.unregister("www.google.com");
        
    } */
}
