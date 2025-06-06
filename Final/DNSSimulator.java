package Final;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DNS simulator for domain name resolution
 * Maps domain names to IP addresses
 */
public class DNSSimulator {
    private final Map<String, String> domainToIp;

    public DNSSimulator() {
        this.domainToIp = new ConcurrentHashMap<>();
        initializeDefaultEntries();
    }

    /**
     * Initialize with some default DNS entries
     */
    private void initializeDefaultEntries() {
        register("localhost", "127.0.0.1");
        register("example.com", "192.0.2.1");
        register("www.example.com", "192.0.2.1");
    }

    /**
     * Register a domain name to IP address mapping
     */
    public void register(String domainName, String ipAddress) {
        domainToIp.put(domainName.toLowerCase(), ipAddress);
        System.out.println("DNS: Registered '" + domainName + "' -> " + ipAddress);
    }

    /**
     * Lookup IP address for a domain name
     */
    public Optional<String> lookup(String domainName) {
        String ip = domainToIp.get(domainName.toLowerCase());
        if (ip != null) {
            System.out.println("DNS: Resolved '" + domainName + "' -> " + ip);
            return Optional.of(ip);
        } else {
            System.out.println("DNS: Cannot resolve '" + domainName + "'");
            return Optional.empty();
        }
    }

    /**
     * Reverse DNS lookup - find domain name for IP
     */
    public Optional<String> reverseLookup(String ipAddress) {
        for (Map.Entry<String, String> entry : domainToIp.entrySet()) {
            if (entry.getValue().equals(ipAddress)) {
                System.out.println("DNS: Reverse lookup " + ipAddress + " -> " + entry.getKey());
                return Optional.of(entry.getKey());
            }
        }
        System.out.println("DNS: No domain found for IP " + ipAddress);
        return Optional.empty();
    }

    /**
     * Remove a domain name entry
     */
    public boolean remove(String domainName) {
        String removed = domainToIp.remove(domainName.toLowerCase());
        if (removed != null) {
            System.out.println("DNS: Removed '" + domainName + "'");
            return true;
        }
        return false;
    }

    /**
     * Get all registered domain names
     */
    public Set<String> getAllDomains() {
        return new HashSet<>(domainToIp.keySet());
    }

    /**
     * Get the number of DNS entries
     */
    public int size() {
        return domainToIp.size();
    }

    /**
     * Check if a domain is registered
     */
    public boolean contains(String domainName) {
        return domainToIp.containsKey(domainName.toLowerCase());
    }

    /**
     * Clear all DNS entries (except defaults)
     */
    public void clear() {
        domainToIp.clear();
        initializeDefaultEntries();
    }

    /**
     * Simulate DNS cache expiration
     */
    public void expireCache() {
        System.out.println("DNS: Cache expired, refreshing entries...");
        // In a real DNS system, this would reload from authoritative servers
        // For simulation, we just print a message
    }

    /**
     * Print all DNS entries
     */
    public void printAll() {
        System.out.println("DNS Simulator entries:");
        List<String> sortedDomains = new ArrayList<>(domainToIp.keySet());
        Collections.sort(sortedDomains);
        
        for (String domain : sortedDomains) {
            System.out.println("  " + domain + " -> " + domainToIp.get(domain));
        }
    }

    /**
     * Simulate DNS query with realistic delay
     */
    public Optional<String> lookupWithDelay(String domainName) {
        try {
            // Simulate network delay for DNS resolution
            Thread.sleep(new Random().nextInt(50) + 10); // 10-60ms delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return lookup(domainName);
    }
} 