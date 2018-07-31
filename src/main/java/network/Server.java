package network;

import org.graphstream.graph.Node;

public class Server {

    private Node nodeParent;
    private String id;
    private int capacity;
    private Double reliability;

    public Server(String id, Node nodeParent, int capacity) {
        this.id = id;
        this.nodeParent = nodeParent;
        this.capacity = capacity;
    }

    public Server(String id, Node nodeParent, int capacity, Double reliability) {
        this.id = id;
        this.nodeParent = nodeParent;
        this.capacity = capacity;
        this.reliability = reliability;
    }

    public Node getNodeParent() {
        return nodeParent;
    }

    public String getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public Double getReliability() {
        return reliability;
    }

    public void setReliability(Double reliability) {
        this.reliability = reliability;
    }
}
