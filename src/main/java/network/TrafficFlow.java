package network;

import filemanager.GraphManager;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Path;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class TrafficFlow {

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
        dijkstra.setSource(GraphManager.getGraph().getNode(src));
        dijkstra.compute();
        Iterator<Path> pathIterator = dijkstra.getAllPathsIterator(GraphManager.getGraph().getNode(dst));
        pathIterator.forEachRemaining(admissiblePaths::add);
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
