package services;


import network.TrafficFlow;

import java.util.List;

public class Service {

    private int id;
    private List<Function> functions;
    private TrafficFlow trafficFlow;

    public Service() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
