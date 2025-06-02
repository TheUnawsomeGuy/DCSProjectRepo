package DNSSim;

import java.util.HashMap;
import java.util.Map;

public class FlatNamingService {
    private Map<String, String> nameToIpMap;

    public FlatNamingService() {
        this.nameToIpMap = new HashMap<>();
    }

    public void register(String name, String ip) {
        nameToIpMap.put(name, ip);
    }

    public String lookup(String name) {
        return nameToIpMap.get(name);
    }
}
