package DNSSim;

import java.util.Map;
import java.util.HashMap;

public class StructuredNamingService {
    private Map<String, String> nameToIpMap;

    public StructuredNamingService() {
        this.nameToIpMap = new HashMap<>();
    }

    public void register(String name, String ip) {
        nameToIpMap.put(name, ip);
    }
}
