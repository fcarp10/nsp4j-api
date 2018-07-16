package services;


import network.TrafficFlow;

import java.util.List;

public class Service {

    private int id;
    private int[] chain;
    private transient List<Function> functions;
    private TrafficFlow trafficFlow;

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
}
