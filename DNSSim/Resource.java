package DNSSim;
/**
 * Resource class represents a network resource with a unique identifier and location.
 * This class is apparently needed for the DNS simulation because it provides the mapping between
 * resource identifiers (like hostnames) and their network locations (like IP addresses).
 * Without this class, the DNS services would lack a structured way to store and 
 * manage resource information needed for name resolution.
 * More research needed to understand why this is needed.
 */

public class Resource { //resource class to store the id and location of the resource (otherwise our mapping would map to what?)
    private String id;
    private String location;

    public Resource(String id, String location) {
        this.id = id;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Resource [id=" + id + ", location=" + location + "]";
    }
}
