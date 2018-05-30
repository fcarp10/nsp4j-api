package network;

import graph.elements.vertex.VertexElement;

public class Server {

    private VertexElement vertexParent;
    private String id;
    private Double capacity;
    private Double reliability;

    public Server(String id, VertexElement vertexParent, Double capacity) {
        this.id = id;
        this.vertexParent = vertexParent;
        this.capacity = capacity;
    }

    public VertexElement getVertexParent() {
        return vertexParent;
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
