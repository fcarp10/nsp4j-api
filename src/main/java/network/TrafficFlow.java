package network;

import filemanager.GraphManager;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class TrafficFlow {

    private static final Logger log = LoggerFactory.getLogger(TrafficFlow.class);
    private String src;
    private String dst;
    private int serviceId;
    private List<Double> trafficDemands;
    private List<Path> admissiblePaths;

    public TrafficFlow() {
        trafficDemands = new ArrayList<>();
        admissiblePaths = new ArrayList<>();
    }

    public void setPaths() {
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.init(GraphManager.getGraph());
        Node srcNode = GraphManager.getGraph().getNode(src);
        Node dstNode = GraphManager.getGraph().getNode(dst);
        if (srcNode != null && dstNode != null) {
            dijkstra.setSource(srcNode);
            dijkstra.compute();
            Iterator<Path> pathIterator = dijkstra.getAllPathsIterator(dstNode);
            pathIterator.forEachRemaining(admissiblePaths::add);
        } else {
            log.error("The source and/or destination node names are wrong");
        }
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

    public List<Double> getTrafficDemands() {
        return trafficDemands;
    }

    public List<Path> getAdmissiblePaths() {
        return admissiblePaths;
    }

    public void setAdmissiblePath(Path admissiblePath) {
        this.admissiblePaths.add(admissiblePath);
    }

    public void setTrafficDemand(Double trafficDemand) {
        trafficDemands.add(trafficDemand);
    }
}
