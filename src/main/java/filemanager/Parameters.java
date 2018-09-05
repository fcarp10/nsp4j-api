package filemanager;


import network.Server;
import network.TrafficFlow;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import services.Function;
import services.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Parameters {

    // Optimization parameters
    private double gap;
    private double[] weights;

    // Network default parameters
    private int serverCapacityDefault;
    private int serversNodeDefault;
    private int linkCapacityDefault;
    private int minPathsDefault;
    private int maxPathsDefault;
    private int minDemandsDefault;
    private int maxDemandsDefault;
    private int minBwDefault;
    private int maxBwDefault;

    // NFV parameters
    private List<Service> serviceChains;
    private List<Function> functions;
    private List<TrafficFlow> trafficFlows;

    // Auxiliary
    private int[] aux;

    // *Local parameters
    private List<Node> nodes;
    private List<Edge> links;
    private List<Server> servers;
    private List<Service> services;
    private List<Path> paths;
    private int pathsPerTrafficFlowAux;
    private int demandsPerTrafficFlowAux;
    private int serviceLengthAux;
    private double totalTrafficAux;
    private int totalNumberOfFunctionsAux;
    private int totalNumberOfPossibleReplicasAux;
    private List<Long> seeds;
    private int seedCounter;
    private String scenario;

    public Parameters() {
        nodes = new ArrayList<>();
        links = new ArrayList<>();
        servers = new ArrayList<>();
        services = new ArrayList<>();
        paths = new ArrayList<>();
        trafficFlows = new ArrayList<>();
        functions = new ArrayList<>();
        serviceChains = new ArrayList<>();
    }

    public void initialize(String path) {
        readSeeds();
        new GraphManager();
        GraphManager.importTopology(path, scenario + ".dgs");
        nodes.addAll(GraphManager.getGraph().getNodeSet());
        links.addAll(GraphManager.getGraph().getEdgeSet());
        setLinkCapacity();
        generateServers();
        mapPathsToTrafficFlows(path);
        mapTrafficDemandsToTrafficFlows();
        createSetOfServices();
        calculateAuxiliaryValues();
    }

    private void setLinkCapacity() {
        for (Edge link : links)
            if (link.getAttribute("capacity") == null)
                link.addAttribute("capacity", linkCapacityDefault);
    }

    private void generateServers() {
        for (Node node : GraphManager.getGraph().getNodeSet()) {
            int serversNode;
            if (node.getAttribute("serversNode") != null)
                serversNode = node.getAttribute("serversNode");
            else
                serversNode = serversNodeDefault;
            for (int s = 0; s < serversNode; s++) {
                Server server;
                if (node.getAttribute("capacity") != null && node.getAttribute("reliability") != null)
                    server = new Server(node.getId() + "-" + s, node, node.getAttribute("capacity"), node.getAttribute("reliability"));
                else if (node.getAttribute("capacity") != null && node.getAttribute("reliability") == null)
                    server = new Server(node.getId() + "-" + s, node, node.getAttribute("capacity"));
                else
                    server = new Server(node.getId() + "-" + s, node, serverCapacityDefault);
                servers.add(server);
            }
        }
    }

    private void mapPathsToTrafficFlows(String path) {
        paths = GraphManager.importPaths(path, scenario + ".txt");
        for (TrafficFlow trafficFlow : trafficFlows)
            trafficFlow.setPaths(paths);
    }

    private void mapTrafficDemandsToTrafficFlows() {
        Random random = new Random();
        for (TrafficFlow trafficFlow : trafficFlows) {
            int numOfTrafficDemands = minDemandsDefault + (maxDemandsDefault - minDemandsDefault) * random.nextInt();
            for (int td = 0; td < numOfTrafficDemands; td++)
                trafficFlow.setTrafficDemand(random.nextInt(maxBwDefault + 1 - minBwDefault) + minBwDefault);
        }
    }

    private void createSetOfServices() {
        for (TrafficFlow trafficFlow : trafficFlows) {
            Service service = getServiceChain(trafficFlow.getServiceId());
            List<Function> functions = new ArrayList<>();
            for (Integer type : service.getChain())
                functions.add(getFunction(type));
            services.add(new Service(service.getId(), functions, trafficFlow));
        }
    }

    private Service getServiceChain(int id) {
        Service service = null;
        for (Service s : serviceChains)
            if (id == s.getId()) {
                service = s;
                break;
            }
        return service;
    }

    private Function getFunction(int type) {
        Function function = null;
        for (Function f : functions)
            if (type == f.getType()) {
                function = f;
                break;
            }
        return function;
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
            for (int trafficDemand : trafficFlow.getTrafficDemands())
                totalTrafficAux += trafficDemand;

        totalNumberOfFunctionsAux = 0;
        for (Service service : services)
            totalNumberOfFunctionsAux += service.getFunctions().size();

        totalNumberOfPossibleReplicasAux = 0;
        for (Service service : services)
            for (Function f : service.getFunctions())
                if (f.isReplicable())
                    totalNumberOfPossibleReplicasAux += maxPathsDefault;
    }

    private void readSeeds() {
        new ConfigFiles();
        Scanner scanner = ConfigFiles.scanPlainTextFileInResources("/aux_files/seeds.txt");
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

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
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

    public double getServerCapacityDefault() {
        return serverCapacityDefault;
    }

    public void setServerCapacityDefault(int serverCapacityDefault) {
        this.serverCapacityDefault = serverCapacityDefault;
    }

    public int getServersNodeDefault() {
        return serversNodeDefault;
    }

    public void setServersNodeDefault(int serversNodeDefault) {
        this.serversNodeDefault = serversNodeDefault;
    }

    public double getLinkCapacityDefault() {
        return linkCapacityDefault;
    }

    public void setLinkCapacityDefault(int linkCapacityDefault) {
        this.linkCapacityDefault = linkCapacityDefault;
    }

    public int getMaxPathsDefault() {
        return maxPathsDefault;
    }

    public void setMaxPathsDefault(int maxPathsDefault) {
        this.maxPathsDefault = maxPathsDefault;
    }

    public int getMinPathsDefault() {
        return minPathsDefault;
    }

    public void setMinPathsDefault(int minPathsDefault) {
        this.minPathsDefault = minPathsDefault;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    public void setServiceChains(List<Service> serviceChains) {
        this.serviceChains = serviceChains;
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

    public int getMinDemandsDefault() {
        return minDemandsDefault;
    }

    public void setMinDemandsDefault(int minDemandsDefault) {
        this.minDemandsDefault = minDemandsDefault;
    }

    public int getMaxDemandsDefault() {
        return maxDemandsDefault;
    }

    public void setMaxDemandsDefault(int maxDemandsDefault) {
        this.maxDemandsDefault = maxDemandsDefault;
    }

    public double getMinBwDefault() {
        return minBwDefault;
    }

    public void setMinBwDefault(int minBwDefault) {
        this.minBwDefault = minBwDefault;
    }

    public double getMaxBwDefault() {
        return maxBwDefault;
    }

    public void setMaxBwDefault(int maxBwDefault) {
        this.maxBwDefault = maxBwDefault;
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

    public int[] getAux() {
        return aux;
    }

    public void setAux(int[] aux) {
        this.aux = aux;
    }
}
