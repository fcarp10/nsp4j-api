package manager.elements;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service {

    private int id;
    private int[] chain;
    private transient List<Function> functions;
    private TrafficFlow trafficFlow;
    private Map<String, Object> attributes;

    public Service() {
        attributes = new HashMap<>();
    }

    public Service(int id, List<Function> functions, TrafficFlow trafficFlow) {
        attributes = new HashMap<>();
        this.id = id;
        this.functions = functions;
        this.trafficFlow = trafficFlow;
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public int getId() {
        return id;
    }

    public int[] getChain() {
        return chain;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public TrafficFlow getTrafficFlow() {
        return trafficFlow;
    }
}
