package filemanager;


import network.Server;
import network.TrafficFlow;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import services.Function;
import services.Service;

import java.math.BigInteger;
import java.util.*;

public class Parameters {

    private String networkFile;
    private String pathsFile;
    private double gap;
    private double[] weights;
    private double serverCapacity;
    private int serversPerNode;
    private double linkCapacity;
    private int maxReplicas;
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
    private List<Path> paths;

    private List<TrafficFlow> trafficFlows;
    private Map<Integer, Function> functionTypes;
    private Map<Integer, Service> serviceTypes;

    private int pathsPerTrafficFlowAux;
    private int demandsPerTrafficFlowAux;
    private int serviceLengthAux;
    private double totalTrafficAux;
    private int totalNumberOfFunctionsAux;
    private int totalNumberOfPossibleReplicasAux;

    public Parameters() {
        nodes = new ArrayList<>();
        links = new ArrayList<>();
        servers = new ArrayList<>();
        services = new ArrayList<>();
        paths = new ArrayList<>();
        trafficFlows = new ArrayList<>();
        functionTypes = new HashMap<>();
        serviceTypes = new HashMap<>();
    }

    public void initialize() {
        readSeeds();
        new GraphManager();
        GraphManager.importTopology(networkFile + ".dgs");
        nodes.addAll(GraphManager.getGraph().getNodeSet());
        links.addAll(GraphManager.getGraph().getEdgeSet());
        setLinkCapacity();
        initializeServers();
        mapPathsToTrafficFlows();
        mapTrafficDemandsToTrafficFlows();
        createSetOfServices();
        calculateAuxiliaryValues();
    }

    private void setLinkCapacity() {
        for (Edge link : links)
            if (link.getAttribute("capacity") == null)
                link.addAttribute("capacity", linkCapacity);
    }

    private void initializeServers() {
        for (Node node : GraphManager.getGraph().getNodeSet())
            for (int s = 0; s < serversPerNode; s++) {
                if (node.getAttribute("capacity") != null)
                    servers.add(new Server(node.getId() + "-" + s, node, node.getAttribute("capacity")));
                else
                    servers.add(new Server(node.getId() + "-" + s, node, serverCapacity));
            }
    }

    private void mapPathsToTrafficFlows() {
        if (pathsFile == null)
            for (TrafficFlow trafficFlow : trafficFlows)
                trafficFlow.setShortestPaths();
        else {
            paths = GraphManager.importPaths(pathsFile + ".txt");
            for (TrafficFlow trafficFlow : trafficFlows)
                trafficFlow.setPaths(paths);
        }
    }

    private void mapTrafficDemandsToTrafficFlows() {
        Random random = new Random();
        for (TrafficFlow trafficFlow : trafficFlows) {
            int numOfTrafficDemands = minDemands + (maxDemands - minDemands) * random.nextInt();
            for (int td = 0; td < numOfTrafficDemands; td++)
                trafficFlow.setTrafficDemand(minBw + (maxBw - minBw) * random.nextDouble());
        }
    }

    private void createSetOfServices() {
        for (TrafficFlow trafficFlow : trafficFlows) {
            Service serviceType = serviceTypes.get(trafficFlow.getServiceId());
            List<Function> functions = new ArrayList<>();
            for (Integer i : serviceType.getChain())
                functions.add(functionTypes.get(i));
            services.add(new Service(serviceType.getId(), functions, trafficFlow));
        }
    }

    private void calculateAuxiliaryValues() {
        pathsPerTrafficFlowAux = 0;
        for (TrafficFlow trafficFlow : trafficFlows)
            if (trafficFlow.getAdmissiblePaths().size() > pathsPerTrafficFlowAux)
                pathsPerTrafficFlowAux = trafficFlow.getAdmissiblePaths().size();

        demandsPerTrafficFlowAux = 0;
        for (TrafficFlow trafficFlow : trafficFlows)
            if (trafficFlow.getTrafficDemands().size() > demandsPerTrafficFlowAux)
                demandsPerTrafficFlowAux = trafficFlow.getTrafficDemands().size();

        serviceLengthAux = 0;
        for (Service service : services)
            if (service.getFunctions().size() > serviceLengthAux)
                serviceLengthAux = service.getFunctions().size();

        for (TrafficFlow trafficFlow : trafficFlows)
            for (Double trafficDemand : trafficFlow.getTrafficDemands())
                totalTrafficAux += trafficDemand;

        totalNumberOfFunctionsAux = 0;
        for (Service service : services)
            totalNumberOfFunctionsAux += service.getFunctions().size();

        totalNumberOfPossibleReplicasAux = 0;
        for (Service service : services)
            for (Function f : service.getFunctions())
                if (f.isReplicable())
                    totalNumberOfPossibleReplicasAux += maxReplicas;
    }

    private void readSeeds() {
        new ConfigFiles();
        Scanner scanner = ConfigFiles.scanPlainTextFileInResources("/seeds.txt");
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

    public String getPathsFile() {
        return pathsFile;
    }

    public void setPathsFile(String pathsFile) {
        this.pathsFile = pathsFile;
    }

    public double getGap() {
        return gap;
    }

    public void setGap(double gap) {
        this.gap = gap;
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
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

    public double getLinkCapacity() {
        return linkCapacity;
    }

    public void setLinkCapacity(double linkCapacity) {
        this.linkCapacity = linkCapacity;
    }

    public int getMaxReplicas() {
        return maxReplicas;
    }

    public void setMaxReplicas(int maxReplicas) {
        this.maxReplicas = maxReplicas;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public void setFunctionTypes(Map<Integer, Function> functionTypes) {
        this.functionTypes = functionTypes;
    }

    public void setServiceTypes(Map<Integer, Service> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public List<TrafficFlow> getTrafficFlows() {
        return trafficFlows;
    }

    public List<Path> getPaths() {
        return paths;
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

    public int getPathsPerTrafficFlowAux() {
        return pathsPerTrafficFlowAux;
    }

    public int getDemandsPerTrafficFlowAux() {
        return demandsPerTrafficFlowAux;
    }

    public int getServiceLengthAux() {
        return serviceLengthAux;
    }

    public double getTotalTrafficAux() {
        return totalTrafficAux;
    }

    public int getTotalNumberOfFunctionsAux() {
        return totalNumberOfFunctionsAux;
    }

    public int getTotalNumberOfPossibleReplicasAux() {
        return totalNumberOfPossibleReplicasAux;
    }
}
