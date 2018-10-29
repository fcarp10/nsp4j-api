package network;

import org.graphstream.graph.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TrafficFlow {

    private String src;
    private String dst;
    private int serviceId;
    private List<Integer> trafficDemands;
    private List<Path> admissiblePaths;
    private int minDemands;
    private int maxDemands;
    private int minBw;
    private int maxBw;

    public TrafficFlow() {
        trafficDemands = new ArrayList<>();
        admissiblePaths = new ArrayList<>();
    }

    public void setPaths(List<Path> allPaths) {
        for (Path path : allPaths)
            if (path.getNodePath().get(0).getId().equals(src) && path.getNodePath().get(path.size() - 1).getId().equals(dst))
                admissiblePaths.add(path);
    }

    public boolean generateTrafficDemands() {
        Random random = new Random();
        if (maxBw > 0 & minBw > 0) {
            int numOfTrafficDemands = minDemands + (maxDemands - minDemands) * random.nextInt();
            for (int td = 0; td < numOfTrafficDemands; td++)
                trafficDemands.add(random.nextInt(maxBw + 1 - minBw) + minBw);
            return true;
        } else return false;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public List<Integer> getTrafficDemands() {
        return trafficDemands;
    }

    public List<Path> getAdmissiblePaths() {
        return admissiblePaths;
    }

    public void setAdmissiblePath(Path admissiblePath) {
        this.admissiblePaths.add(admissiblePath);
    }

    public void setTrafficDemand(Integer trafficDemand) {
        trafficDemands.add(trafficDemand);
    }

    public int getMinDemands() {
        return minDemands;
    }

    public void setMinDemands(int minDemands) {
        this.minDemands = minDemands;
    }

    public int getMaxDemands() {
        return maxDemands;
    }

    public void setMaxDemands(int maxDemands) {
        this.maxDemands = maxDemands;
    }

    public int getMinBw() {
        return minBw;
    }

    public void setMinBw(int minBw) {
        this.minBw = minBw;
    }

    public int getMaxBw() {
        return maxBw;
    }

    public void setMaxBw(int maxBw) {
        this.maxBw = maxBw;
    }
}
