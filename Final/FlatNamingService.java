package Final;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Flat naming service using hash table (dictionary) approach
 * Provides simple key-value mapping for resource names
 */
public class FlatNamingService {
    private final Map<String, Resource> nameToResource;

    public FlatNamingService() {
        this.nameToResource = new ConcurrentHashMap<>();
    }

    /**
     * Register a resource with a flat name
     */
    public void register(String name, Resource resource) {
        nameToResource.put(name.toLowerCase(), resource);
        System.out.println("Flat naming: Registered '" + name + "' -> " + resource.getLocation());
    }

    /**
     * Lookup a resource by flat name
     */
    public Optional<String> lookup(String name) {
        Resource resource = nameToResource.get(name.toLowerCase());
        if (resource != null) {
            System.out.println("Flat naming: Found '" + name + "' -> " + resource.getLocation());
            return Optional.of(resource.getLocation());
        } else {
            System.out.println("Flat naming: Not found '" + name + "'");
            return Optional.empty();
        }
    }

    /**
     * Remove a resource from the naming service
     */
    public boolean remove(String name) {
        Resource removed = nameToResource.remove(name.toLowerCase());
        if (removed != null) {
            System.out.println("Flat naming: Removed '" + name + "'");
            return true;
        }
        return false;
    }

    /**
     * Get all registered names
     */
    public Set<String> getAllNames() {
        return new HashSet<>(nameToResource.keySet());
    }

    /**
     * Get the number of entries
     */
    public int size() {
        return nameToResource.size();
    }

    /**
     * Check if a name exists
     */
    public boolean contains(String name) {
        return nameToResource.containsKey(name.toLowerCase());
    }

    /**
     * Clear all entries
     */
    public void clear() {
        nameToResource.clear();
    }

    /**
     * Print all entries
     */
    public void printAll() {
        System.out.println("Flat Naming Service entries:");
        for (Map.Entry<String, Resource> entry : nameToResource.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue().getLocation());
        }
    }
} 