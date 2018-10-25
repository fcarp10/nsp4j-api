package network;

import org.graphstream.graph.Node;

public class Server {

    private Node nodeParent;
    private String id;
    private int capacity;
    private Double reliability;
    private int processingDelay;

    public Server(String id, Node nodeParent, int capacity, int processingDelay) {
        this.id = id;
        this.nodeParent = nodeParent;
        this.capacity = capacity;
        this.processingDelay = processingDelay;
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

    public int getProcessingDelay() {
        return processingDelay;
    }


    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setReliability(Double reliability) {
        this.reliability = reliability;
    }

    public void setProcessingDelay(int processingDelay) {
        this.processingDelay = processingDelay;
    }
}
