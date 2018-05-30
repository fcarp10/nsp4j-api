package network;


import graph.path.PathElement;

import java.util.ArrayList;
import java.util.List;

public class TrafficFlow {

    private String source;
    private String destination;
    private int serviceId;
    private List<Double> trafficDemands;
    private List<PathElement> admissiblePaths;

    public TrafficFlow() {
        trafficDemands = new ArrayList<>();
        admissiblePaths = new ArrayList<>();
    }

    public TrafficFlow(String source, String destination) {
        this.source = source;
        this.destination = destination;
        trafficDemands = new ArrayList<>();
        admissiblePaths = new ArrayList<>();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public List<Double> getTrafficDemands() {
        return trafficDemands;
    }

    public List<PathElement> getAdmissiblePaths() {
        return admissiblePaths;
    }

    public void setAdmissiblePath(PathElement admissiblePath) {
        this.admissiblePaths.add(admissiblePath);
    }

    public void setTrafficDemand(Double trafficDemand) {
        trafficDemands.add(trafficDemand);
    }
}
