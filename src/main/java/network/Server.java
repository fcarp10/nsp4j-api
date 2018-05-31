package network;

import org.graphstream.graph.Node;

public class Server {

    private Node nodeParent;
    private String id;
    private Double capacity;
    private Double reliability;

    public Server(String id, Node nodeParent, Double capacity) {
        this.id = id;
        this.nodeParent = nodeParent;
        this.capacity = capacity;
    }

    public Node getVertexParent() {
        return nodeParent;
    }

    public String getId() {
        return id;
    }

    public Double getCapacity() {
        return capacity;
    }

    public Double getReliability() {
        return reliability;
    }

    public void setReliability(Double reliability) {
        this.reliability = reliability;
    }
}
