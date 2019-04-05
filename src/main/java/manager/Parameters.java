package manager;

import manager.elements.Function;
import manager.elements.Server;
import manager.elements.Service;
import manager.elements.TrafficFlow;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ConfigFiles;
import utils.GraphManager;

import java.math.BigInteger;
import java.util.*;

import static utils.Definitions.*;

public class Parameters {

   // auxiliary parameters
   private Map aux;
   // service definitions
   private List<Service> serviceChains;
   // function definitions
   private List<Function> functionTypes;
   // traffic flow definitions
   private List<TrafficFlow> trafficFlows;
   // local parameters
   private Graph graph;
   private List<Node> nodes;
   private List<Edge> links;
   private List<Server> servers;
   private List<Service> services;
   private List<Path> paths;
   private int pathsTrafficFlow;
   private int demandsTrafficFlow;
   private int serviceLength;
   private double totalTraffic;
   private int totalNumFunctions;
   private int totalNumPossibleRep;
   private List<Long> seeds;
   private int seedCounter;
   private String scenario;
   private Random rnd;
   private static final Logger log = LoggerFactory.getLogger(Parameters.class);

   public Parameters() {
      nodes = new ArrayList<>();
      links = new ArrayList<>();
      servers = new ArrayList<>();
      services = new ArrayList<>();
      paths = new ArrayList<>();
      trafficFlows = new ArrayList<>();
      functionTypes = new ArrayList<>();
      serviceChains = new ArrayList<>();
      aux = new HashMap();
   }

   public void initialize(String path) {
      readSeeds();
      rnd = new Random(getSeed());
      new GraphManager();
      graph = GraphManager.importTopology(path, scenario);
      try {
         nodes.addAll(graph.getNodeSet());
         links.addAll(graph.getEdgeSet());
         setLinkParameters();
         generateServers();
         generateTrafficFlows(path);
         createSetOfServices();
         calculateAuxiliaryValues();
      } catch (Exception e) {
         log.error("error generating parameters: " + e.toString());
         System.exit(1);
      }
   }

   private void setLinkParameters() {
      for (Edge link : links) {
         if (link.getAttribute(LINK_CAPACITY) == null)
            link.addAttribute(LINK_CAPACITY, (int) links.get(0).getAttribute(LINK_CAPACITY));
         if (link.getAttribute(LINK_DELAY) == null) {
            int n1X = link.getSourceNode().getAttribute("x");
            int n1Y = link.getSourceNode().getAttribute("y");
            int n2X = link.getTargetNode().getAttribute("x");
            int n2Y = link.getTargetNode().getAttribute("y");
            double delay = Math.sqrt(Math.pow(n1X - n2X, 2) + Math.pow(n1Y - n2Y, 2)) / 29.9792458;
            link.addAttribute(LINK_DELAY, delay);
         }
      }
   }

   private void generateServers() {
      for (Node n : nodes) {
         if (n.getAttribute(NODE_NUM_SERVERS) == null)
            n.addAttribute(NODE_NUM_SERVERS, (int) nodes.get(0).getAttribute(NODE_NUM_SERVERS));
         for (int s = 0; s < (int) n.getAttribute(NODE_NUM_SERVERS); s++) {
            if (n.getAttribute(NODE_SERVER_CAP) == null)
               n.addAttribute(NODE_SERVER_CAP, (int) nodes.get(0).getAttribute(NODE_SERVER_CAP));
            if (n.getAttribute(SERVER_PROCESS_DELAY) == null)
               n.addAttribute(SERVER_PROCESS_DELAY, (int) nodes.get(0).getAttribute(SERVER_PROCESS_DELAY));
            servers.add(new Server(n.getId() + "_" + s, n, n.getAttribute(NODE_SERVER_CAP), n.getAttribute(SERVER_PROCESS_DELAY)));
         }
      }
   }

   private void generateTrafficFlows(String path) {
      paths = GraphManager.importPaths(graph, path, scenario + ".txt");
      TrafficFlow t = trafficFlows.get(0);
      if (t.getSrc() == null && t.getDst() == null) {
         for (Node src : nodes)
            for (Node dst : nodes) {
               if (src == dst) continue;
               TrafficFlow tf = new TrafficFlow(src.getId(), dst.getId()
                       , serviceChains.get(rnd.nextInt(serviceChains.size())).getId());
               int numOfTrafficDemands = rnd.nextInt(t.getMaxDem() + 1 - t.getMinDem()) + t.getMinDem();
               for (int td = 0; td < numOfTrafficDemands; td++)
                  tf.setTrafficDemand(rnd.nextInt(t.getMaxBw() + 1 - t.getMinBw()) + t.getMinBw());
               tf.setPaths(paths);
               trafficFlows.add(tf);
            }
         trafficFlows.remove(0);
      } else
         for (TrafficFlow trafficFlow : trafficFlows) {
            trafficFlow.generateTrafficDemands(rnd);
            trafficFlow.setPaths(paths);
         }
   }

   private void createSetOfServices() {
      for (TrafficFlow trafficFlow : trafficFlows) {
         Service serviceChain = getServiceChain(trafficFlow.getServiceId());
         List<Function> functions = new ArrayList<>();
         for (Integer type : serviceChain.getChain())
            functions.add(getFunction(type));
         services.add(new Service(serviceChain, functions, trafficFlow));
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
      for (Function f : functionTypes)
         if (type == f.getType()) {
            function = f;
            break;
         }
      return function;
   }

   private void calculateAuxiliaryValues() {
      pathsTrafficFlow = 0;
      for (TrafficFlow trafficFlow : trafficFlows)
         if (trafficFlow.getPaths().size() > pathsTrafficFlow)
            pathsTrafficFlow = trafficFlow.getPaths().size();

      demandsTrafficFlow = 0;
      for (TrafficFlow trafficFlow : trafficFlows)
         if (trafficFlow.getDemands().size() > demandsTrafficFlow)
            demandsTrafficFlow = trafficFlow.getDemands().size();

      serviceLength = 0;
      for (Service service : services)
         if (service.getFunctions().size() > serviceLength)
            serviceLength = service.getFunctions().size();

      for (TrafficFlow trafficFlow : trafficFlows)
         for (int trafficDemand : trafficFlow.getDemands())
            totalTraffic += trafficDemand;

      totalNumFunctions = 0;
      for (Service service : services)
         totalNumFunctions += service.getFunctions().size();

      totalNumPossibleRep = 0;
      for (Service service : services)
         for (Function f : service.getFunctions())
            if ((boolean) f.getAttribute(FUNCTION_REPLICABLE))
               totalNumPossibleRep += (int) service.getAttribute(SERVICE_MAX_PATHS);
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

   public List<Service> getServices() {
      return services;
   }

   public void setServices(List<Service> services) {
      this.services = services;
   }

   public List<Function> getFunctionTypes() {
      return functionTypes;
   }

   public void setFunctionTypes(List<Function> functionTypes) {
      this.functionTypes = functionTypes;
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

   public int getPathsTrafficFlow() {
      return pathsTrafficFlow;
   }

   public int getDemandsTrafficFlow() {
      return demandsTrafficFlow;
   }

   public int getServiceLength() {
      return serviceLength;
   }

   public double getTotalTraffic() {
      return totalTraffic;
   }

   public int getTotalNumFunctions() {
      return totalNumFunctions;
   }

   public int getTotalNumPossibleRep() {
      return totalNumPossibleRep;
   }

   public Map getAux() {
      return aux;
   }
}
