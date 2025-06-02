package DNSSim;

import java.util.Map;
import java.util.HashMap;

public class DNSSimulator {
    
    private Map<String, String> dnsRecords;

    public DNSSimulator() {
        this.dnsRecords = new HashMap<>();
    }

    public void register(String name, String ip) {
        dnsRecords.put(name, ip);
    }
    
    public static void main(String[] args) {
        System.out.println("DNS Simulator");
    }
}
