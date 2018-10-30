package manager.elements;


import java.util.List;

public class Service {

    private int id;
    private int[] chain;
    private transient List<Function> functions;
    private TrafficFlow trafficFlow;
    private int minPaths;
    private int maxPaths;

    public Service() {
    }

    public Service(int id, List<Function> functions, TrafficFlow trafficFlow) {
        this.id = id;
        this.functions = functions;
        this.trafficFlow = trafficFlow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getChain() {
        return chain;
    }

    public void setChain(int[] chain) {
        this.chain = chain;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    public TrafficFlow getTrafficFlow() {
        return trafficFlow;
    }

    public void setTrafficFlow(TrafficFlow trafficFlow) {
        this.trafficFlow = trafficFlow;
    }

    public int getMinPaths() {
        return minPaths;
    }

    public void setMinPaths(int minPaths) {
        this.minPaths = minPaths;
    }

    public int getMaxPaths() {
        return maxPaths;
    }

    public void setMaxPaths(int maxPaths) {
        this.maxPaths = maxPaths;
    }
}
