package manager;

import manager.elements.Server;
import manager.elements.TrafficFlow;
import utils.ConfigFiles;
import utils.GraphManager;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import manager.elements.Function;
import manager.elements.Service;

import java.math.BigInteger;
import java.util.*;

public class Parameters {

    // Optimization parameters
    private double gap;
    private double[] weights;

    // Network default parameters
    private int serverCapacityDefault;
    private int serversNodeDefault;
    private int serverDelayDefault;
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
    private Map aux;

    // *Local parameters
    private Graph graph;
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
        aux = new HashMap();
    }

    public void initialize(String path) {
        readSeeds();
        new GraphManager();
        graph = GraphManager.importTopology(path, scenario);
        nodes.addAll(graph.getNodeSet());
        links.addAll(graph.getEdgeSet());
        setLinkParameters();
        generateServers();
        generateTrafficFlows();
        mapPathsToTrafficFlows(path);
        mapTrafficDemandsToTrafficFlows();
        createSetOfServices();
        calculateAuxiliaryValues();
    }

    private void setLinkParameters() {
        for (Edge link : links) {
            if (link.getAttribute("capacity") == null)
                link.addAttribute("capacity", linkCapacityDefault);
            if (link.getAttribute("delay") == null) {
                int n1X = link.getSourceNode().getAttribute("x");
                int n1Y = link.getSourceNode().getAttribute("y");
                int n2X = link.getTargetNode().getAttribute("x");
                int n2Y = link.getTargetNode().getAttribute("y");
                double delay = Math.sqrt(Math.pow(n1X - n2X, 2) + Math.pow(n1Y - n2Y, 2)) / 29.9792458;
                link.addAttribute("delay", delay);
            }
        }
    }

    private void generateServers() {
        for (Node node : graph.getNodeSet()) {
            int serversNode;
            if (node.getAttribute("serversNode") != null)
                serversNode = node.getAttribute("serversNode");
            else
                serversNode = serversNodeDefault;
            for (int s = 0; s < serversNode; s++) {
                Server server = new Server(node.getId() + "_" + s, node, serverCapacityDefault, serverDelayDefault);
                if (node.getAttribute("capacity") != null)
                    server.setCapacity(node.getAttribute("capacity"));
                if (node.getAttribute("delay") != null)
                    server.setProcessingDelay(node.getAttribute("delay"));
                if (node.getAttribute("reliability") != null)
                    server.setReliability(node.getAttribute("reliability"));
                servers.add(server);
            }
        }
    }

    private void generateTrafficFlows() {
        Random random = new Random();
        if (trafficFlows.size() == 0) {
            for (Node srcNode : nodes)
                for (Node dstNode : nodes) {
                    if (srcNode == dstNode) continue;
                    trafficFlows.add(new TrafficFlow(srcNode.getId(), dstNode.getId()
                            , serviceChains.get(random.nextInt(serviceChains.size())).getId()));
                }
        }
    }

    private void mapPathsToTrafficFlows(String path) {
        paths = GraphManager.importPaths(graph, path, scenario + ".txt");
        for (TrafficFlow trafficFlow : trafficFlows)
            trafficFlow.setPaths(paths);
    }

    private void mapTrafficDemandsToTrafficFlows() {
        Random random = new Random();
        for (TrafficFlow trafficFlow : trafficFlows)
            if (!trafficFlow.generateTrafficDemands()) {
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
                if ((boolean) f.getAttribute("replicable"))
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

    public Object getAux(String key) {
        return aux.get(key);
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

    public int getServerDelayDefault() {
        return serverDelayDefault;
    }

    public void setServerDelayDefault(int serverDelayDefault) {
        this.serverDelayDefault = serverDelayDefault;
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

    public Map getAux() {
        return aux;
    }
}
