package Final;

/**
 * Represents a resource in the distributed system
 * Used by naming services to map names to locations
 */
public class Resource {
    private final String resourceId;
    private final String location;

    public Resource(String resourceId, String location) {
        this.resourceId = resourceId;
        this.location = location;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return String.format("Resource[id=%s, location=%s]", resourceId, location);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Resource resource = (Resource) obj;
        return resourceId.equals(resource.resourceId) && location.equals(resource.location);
    }

    @Override
    public int hashCode() {
        return resourceId.hashCode() + location.hashCode();
    }
} 