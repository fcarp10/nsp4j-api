package filemanager;


import graph.path.PathElement;
import network.EndPoint;
import services.Function;
import services.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class InputParameters {

    private String networkFile;
    private boolean bidirectionalLinks;
    private int minReplicas;
    private int maxReplicas;
    private double gap;
    private double alpha;
    private double beta;
    private double serverCapacity;
    private List<Service> services;
    private List<EndPoint> endPoints;
    private int minDemmands;
    private int maxDemmands;
    private double minBw;
    private double maxBw;

    private int seedCounter;
    private List<Long> seeds;

    public InputParameters() {
    }

    public void readSeeds(String file) {
        Scanner scanner = new ConfigFiles().scanPlainTextFile(file);
        seeds = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.startsWith("#")) {
                line = line.replaceAll("\\s+", "");
                long seed = new BigInteger(line, 2).longValue();
                seeds.add(seed);
            }
        }
    }

    public void setupPathsToEndPoints(List<PathElement> paths) {
        for (PathElement p : paths)
            for (EndPoint endPoint : endPoints)
                if (p.getSourceID().equals(endPoint.getSource()) && p.getDestinationID().equals(endPoint.getDestination())) {
                    endPoint.setAdmissiblePath(p);
                    break;
                }
    }

    public int getNumOfFunctionTypes() {
        int counter = 0;
        List<String> functionTypes = new ArrayList<>();

        for (Service service : services)
            for (Function function : service.getFunctions())
                if (!functionTypes.contains(function.getId())) {
                    functionTypes.add(function.getId());
                    counter++;
                }
        return counter;
    }

    public void setupTrafficDemands() {
        Random random = new Random();
        for (EndPoint endPoint : endPoints)
            endPoint.setTrafficDemand(minBw + (maxBw - minBw) * random.nextDouble());
    }

    public Long getSeed() {
        seedCounter++;
        return seeds.get(seedCounter);
    }

    public String getNetworkFile() {
        return networkFile;
    }

    public void setNetworkFile(String networkFile) {
        this.networkFile = networkFile;
    }

    public boolean isBidirectionalLinks() {
        return bidirectionalLinks;
    }

    public void setBidirectionalLinks(boolean bidirectionalLinks) {
        this.bidirectionalLinks = bidirectionalLinks;
    }

    public int getMinReplicas() {
        return minReplicas;
    }

    public void setMinReplicas(int minReplicas) {
        this.minReplicas = minReplicas;
    }

    public int getMaxReplicas() {
        return maxReplicas;
    }

    public void setMaxReplicas(int maxReplicas) {
        this.maxReplicas = maxReplicas;
    }

    public double getGap() {
        return gap;
    }

    public void setGap(double gap) {
        this.gap = gap;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getServerCapacity() {
        return serverCapacity;
    }

    public void setServerCapacity(double serverCapacity) {
        this.serverCapacity = serverCapacity;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<EndPoint> getEndPoints() {
        return endPoints;
    }

    public void setEndPoints(List<EndPoint> endPoints) {
        this.endPoints = endPoints;
    }

    public int getMinDemmands() {
        return minDemmands;
    }

    public void setMinDemmands(int minDemmands) {
        this.minDemmands = minDemmands;
    }

    public int getMaxDemmands() {
        return maxDemmands;
    }

    public void setMaxDemmands(int maxDemmands) {
        this.maxDemmands = maxDemmands;
    }

    public double getMinBw() {
        return minBw;
    }

    public void setMinBw(double minBw) {
        this.minBw = minBw;
    }

    public double getMaxBw() {
        return maxBw;
    }

    public void setMaxBw(double maxBw) {
        this.maxBw = maxBw;
    }
}
