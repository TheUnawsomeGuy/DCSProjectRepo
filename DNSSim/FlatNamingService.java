package DNSSim;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FlatNamingService {
    private Map<String, Resource> flatRegistery; //Map to store the resource name and the resource object

    public FlatNamingService() {
        this.flatRegistery = new HashMap<>();
    }

    public boolean register(String name, Resource resource) {
        if (flatRegistery.containsKey(name)) {
            throw new IllegalArgumentException("Resource already registered");
        } else {
            flatRegistery.put(name, resource);
            System.out.println("Registered: " + name + " -> " + resource.getLocation());
            return true;
        }
    }

    public boolean unregister(String name) {
        if (flatRegistery.containsKey(name)) {
            flatRegistery.remove(name);
            System.out.println("Unregistered: " + name);
            return true;
        } else {
            System.out.println("Unregistration Failed: " + name);
            return false;
        }
    }

    /*
     * lookup method to lookup a resource by name
     * returns an optional string of the resource location
     * if the resource is not found, returns an empty optional
     * Dev note: I just learned about Optional. Apparently it's something that can be used to avoid null being returned.
     * https://www.baeldung.com/java-optional
     */

    public Optional<String> lookup(String name) {
        Resource resource = flatRegistery.get(name);
        if (resource == null) {
            System.out.println("Name Lookup Failed: " + name);
            return Optional.empty();
        } else {
            System.out.println("Name Lookup Successful: " + name + " -> " + resource.getLocation());
            return Optional.of(resource.getLocation());
        }
    }
}
