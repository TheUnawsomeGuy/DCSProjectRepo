package Final;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Structured naming service using hierarchical paths
 * Similar to DNS or file system paths
 */
public class StructuredNamingService {
    private final Map<String, Resource> pathToResource;

    public StructuredNamingService() {
        this.pathToResource = new ConcurrentHashMap<>();
    }

    /**
     * Register a resource with a hierarchical path
     */
    public void register(String path, Resource resource) {
        if (!isValidPath(path)) {
            throw new IllegalArgumentException("Invalid path format: " + path + ". Must start with '/'");
        }
        
        pathToResource.put(path.toLowerCase(), resource);
        System.out.println("Structured naming: Registered '" + path + "' -> " + resource.getLocation());
    }

    /**
     * Lookup a resource by hierarchical path
     */
    public Optional<String> lookup(String path) {
        if (!isValidPath(path)) {
            System.out.println("Structured naming: Invalid path format '" + path + "'");
            return Optional.empty();
        }

        Resource resource = pathToResource.get(path.toLowerCase());
        if (resource != null) {
            System.out.println("Structured naming: Found '" + path + "' -> " + resource.getLocation());
            return Optional.of(resource.getLocation());
        } else {
            System.out.println("Structured naming: Not found '" + path + "'");
            return Optional.empty();
        }
    }

    /**
     * Remove a resource from the naming service
     */
    public boolean remove(String path) {
        Resource removed = pathToResource.remove(path.toLowerCase());
        if (removed != null) {
            System.out.println("Structured naming: Removed '" + path + "'");
            return true;
        }
        return false;
    }

    /**
     * List all resources under a specific path prefix
     */
    public List<String> listChildren(String parentPath) {
        if (!isValidPath(parentPath)) {
            return new ArrayList<>();
        }

        List<String> children = new ArrayList<>();
        String prefix = parentPath.toLowerCase();
        if (!prefix.endsWith("/")) {
            prefix += "/";
        }

        for (String path : pathToResource.keySet()) {
            if (path.startsWith(prefix) && !path.equals(parentPath.toLowerCase())) {
                // Find immediate children only
                String relativePath = path.substring(prefix.length());
                int slashIndex = relativePath.indexOf('/');
                String childName = slashIndex == -1 ? relativePath : relativePath.substring(0, slashIndex);
                
                if (!children.contains(childName)) {
                    children.add(childName);
                }
            }
        }

        return children;
    }

    /**
     * Validate path format
     */
    private boolean isValidPath(String path) {
        return path != null && path.startsWith("/");
    }

    /**
     * Get all registered paths
     */
    public Set<String> getAllPaths() {
        return new HashSet<>(pathToResource.keySet());
    }

    /**
     * Get the number of entries
     */
    public int size() {
        return pathToResource.size();
    }

    /**
     * Check if a path exists
     */
    public boolean contains(String path) {
        return pathToResource.containsKey(path.toLowerCase());
    }

    /**
     * Clear all entries
     */
    public void clear() {
        pathToResource.clear();
    }

    /**
     * Print all entries in a tree-like format
     */
    public void printAll() {
        System.out.println("Structured Naming Service entries:");
        List<String> sortedPaths = new ArrayList<>(pathToResource.keySet());
        Collections.sort(sortedPaths);
        
        for (String path : sortedPaths) {
            Resource resource = pathToResource.get(path);
            int depth = path.split("/").length - 1;
            String indent = "  ".repeat(depth);
            System.out.println(indent + path + " -> " + resource.getLocation());
        }
    }
} 