package DNSSim;

import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

public class StructuredNamingService {
    private Map<String, Resource> structuredRegistery;

    public StructuredNamingService() {
        this.structuredRegistery = new HashMap<>();
    }

    public boolean register(String path, Resource resource) {
        if (path == null || path.trim().isEmpty() || !path.startsWith("/")) {
            System.out.println("Error: Invalid Path: " + path);
            return false;
        }
        if (structuredRegistery.containsKey(path)) {
            System.out.println("Error: Path already registered: " + path);
            return false;
        } else {
            structuredRegistery.put(path, resource);
            System.out.println("Registered: " + path + " -> " + resource.getLocation());
            return true;
        }
    }

    public boolean unregister(String path) {
        if (structuredRegistery.containsKey(path)) {
            structuredRegistery.remove(path);
            System.out.println("Unregistered: " + path);
            return true;
        } else {
            System.out.println("Unregistration Failed: " + path);
            return false;
        }
    }

    public Optional<String> lookup(String path) {
        Resource resource = structuredRegistery.get(path);
        if (resource == null) {
            System.out.println("Name Lookup Failed: " + path);
            return Optional.empty();
        } else {
            System.out.println("Name Lookup Successful: " + path + " -> " + resource.getLocation());
            return Optional.of(resource.getLocation());
        }
    }
}
