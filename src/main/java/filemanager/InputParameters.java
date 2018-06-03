package filemanager;


import network.Server;
import network.TrafficFlow;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
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
    private int serversPerNode;
    private int minDemands;
    private int maxDemands;
    private double minBw;
    private double maxBw;
    private int seedCounter;
    private List<Long> seeds;

    private List<Node> nodes;
    private List<Edge> links;
    private List<Server> servers;
    private List<Service> services;
    private List<TrafficFlow> trafficFlows;

    private int auxPathsPerTrafficFlow;
    private int auxDemandsPerTrafficFlow;
    private int auxServiceLength;
    private double auxTotalTraffic;

    public InputParameters() {
        nodes = new ArrayList<>();
        links = new ArrayList<>();
        servers = new ArrayList<>();
        services = new ArrayList<>();
        trafficFlows = new ArrayList<>();
    }

    public void initializeParameters() {
        readSeeds("seeds.txt");
        GraphManager.importTopology(networkFile + ".dgs");
        nodes.addAll(GraphManager.getGraph().getNodeSet());
        links.addAll(GraphManager.getGraph().getEdgeSet());
        initializeServers();
        mapPathsToTrafficFlows();
        mapTrafficDemandsToTrafficFlows();
        mapTrafficFlowsToServices();
        calculateAuxiliaryValues();
    }

    private void mapPathsToTrafficFlows() {
        for (TrafficFlow trafficFlow : trafficFlows)
            trafficFlow.setPaths();
    }

    private void mapTrafficDemandsToTrafficFlows() {
        Random random = new Random();
        for (TrafficFlow trafficFlow : trafficFlows)
            trafficFlow.setTrafficDemand(minBw + (maxBw - minBw) * random.nextDouble());
    }

    private void mapTrafficFlowsToServices() {
        for (TrafficFlow trafficFlow : trafficFlows)
            for (Service service : services)
                if (trafficFlow.getServiceId() == service.getId())
                    service.setTrafficFlow(trafficFlow);
    }

    private void initializeServers() {
        for (Node node : GraphManager.getGraph().getNodeSet())
            for (int s = 0; s < serversPerNode; s++) {
                servers.add(new Server(node.getId() + "-" + s, node, serverCapacity));
            }
    }

    private void calculateAuxiliaryValues() {
        auxPathsPerTrafficFlow = 0;
        for (TrafficFlow trafficFlow : trafficFlows)
            if (trafficFlow.getAdmissiblePaths().size() > auxPathsPerTrafficFlow)
                auxPathsPerTrafficFlow = trafficFlow.getAdmissiblePaths().size();

        auxDemandsPerTrafficFlow = 0;
        for (TrafficFlow trafficFlow : trafficFlows)
            if (trafficFlow.getTrafficDemands().size() > auxDemandsPerTrafficFlow)
                auxDemandsPerTrafficFlow = trafficFlow.getTrafficDemands().size();

        auxServiceLength = 0;
        for (Service service : services)
            if (service.getFunctions().size() > auxServiceLength)
                auxServiceLength = service.getFunctions().size();

        for (TrafficFlow trafficFlow : trafficFlows)
            for (Double trafficDemand : trafficFlow.getTrafficDemands())
                auxTotalTraffic += trafficDemand;
    }

    private void readSeeds(String file) {
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

    public int getServersPerNode() {
        return serversPerNode;
    }

    public void setServersPerNode(int serversPerNode) {
        this.serversPerNode = serversPerNode;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<TrafficFlow> getTrafficFlows() {
        return trafficFlows;
    }

    public void setTrafficFlows(List<TrafficFlow> trafficFlows) {
        this.trafficFlows = trafficFlows;
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

    public List<Server> getServers() {
        return servers;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getLinks() {
        return links;
    }

    public int getAuxPathsPerTrafficFlow() {
        return auxPathsPerTrafficFlow;
    }

    public int getAuxDemandsPerTrafficFlow() {
        return auxDemandsPerTrafficFlow;
    }

    public int getAuxServiceLength() {
        return auxServiceLength;
    }

    public double getAuxTotalTraffic() {
        return auxTotalTraffic;
    }
}
