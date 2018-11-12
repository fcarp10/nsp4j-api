package manager;

import manager.elements.Server;
import manager.elements.TrafficFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    // optimization parameters
    private double gap;
    private double[] weights;
    // auxiliary parameters
    private Map aux;
    // service definitions
    private List<Service> serviceChains;
    // function definitions
    private List<Function> functions;
    // traffic flow definitions
    private List<TrafficFlow> trafficFlows;

    // local parameters
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

    private final String CAPACITY = "capacity";
    private final String NUM_SERVERS = "num_servers";
    private final String SERVER_CAPACITY = "server_capacity";
    private final String DELAY = "delay";
    private final String PROCESSING_DELAY = "processing_delay";

    private static final Logger log = LoggerFactory.getLogger(Parameters.class);

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
        try{
            nodes.addAll(graph.getNodeSet());
            links.addAll(graph.getEdgeSet());
            setLinkParameters();
            generateServers();
            generateTrafficFlows(path);
            createSetOfServices();
            calculateAuxiliaryValues();
        } catch (Exception e){
            log.error("while generating network parameters");
        }

    }

    private void setLinkParameters() {
        for (Edge link : links) {
            if (link.getAttribute(CAPACITY) == null)
                link.addAttribute(CAPACITY, (int) links.get(0).getAttribute(CAPACITY));
            if (link.getAttribute(DELAY) == null) {
                int n1X = link.getSourceNode().getAttribute("x");
                int n1Y = link.getSourceNode().getAttribute("y");
                int n2X = link.getTargetNode().getAttribute("x");
                int n2Y = link.getTargetNode().getAttribute("y");
                double delay = Math.sqrt(Math.pow(n1X - n2X, 2) + Math.pow(n1Y - n2Y, 2)) / 29.9792458;
                link.addAttribute(DELAY, delay);
            }
        }
    }

    private void generateServers() {
        for (Node node : nodes) {
            if (node.getAttribute(NUM_SERVERS) == null)
                node.addAttribute(NUM_SERVERS, (int) nodes.get(0).getAttribute(NUM_SERVERS));
            for (int s = 0; s < (int) node.getAttribute(NUM_SERVERS); s++) {
                if (node.getAttribute(SERVER_CAPACITY) == null)
                    node.addAttribute(SERVER_CAPACITY, (int) nodes.get(0).getAttribute(SERVER_CAPACITY));
                if (node.getAttribute(PROCESSING_DELAY) == null)
                    node.addAttribute(PROCESSING_DELAY, (int) nodes.get(0).getAttribute(PROCESSING_DELAY));
                Server server = new Server(node.getId() + "_" + s, node, node.getAttribute(SERVER_CAPACITY), node.getAttribute(PROCESSING_DELAY));
                servers.add(server);
            }
        }
    }

    private void generateTrafficFlows(String path) {
        paths = GraphManager.importPaths(graph, path, scenario + ".txt");
        Random random = new Random();
        if (trafficFlows.get(0).getSrc() == null && trafficFlows.get(0).getDst() == null) {
            for (Node srcNode : nodes)
                for (Node dstNode : nodes) {
                    if (srcNode == dstNode) continue;
                    TrafficFlow trafficFlow = new TrafficFlow(srcNode.getId(), dstNode.getId(), serviceChains.get(random.nextInt(serviceChains.size())).getId());
                    int numOfTrafficDemands = random.nextInt(trafficFlows.get(0).getMaxDemands() + 1 - trafficFlows.get(0).getMinDemands()) + trafficFlows.get(0).getMinDemands();
                    for (int td = 0; td < numOfTrafficDemands; td++)
                        trafficFlow.setTrafficDemand(random.nextInt(trafficFlows.get(0).getMaxBw()
                                + 1 - trafficFlows.get(0).getMinBw()) + trafficFlows.get(0).getMinBw());
                    trafficFlow.setPaths(paths);
                    trafficFlows.add(trafficFlow);
                }
            trafficFlows.remove(0);
        } else {
            for (TrafficFlow trafficFlow : trafficFlows) {
                trafficFlow.generateTrafficDemands();
                trafficFlow.setPaths(paths);
            }
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
                    totalNumberOfPossibleReplicasAux += (int) aux.get("maxPathsDefault");
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
