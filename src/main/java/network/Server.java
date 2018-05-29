package network;

import graph.elements.vertex.VertexElement;

public class Server {

    protected VertexElement vertexParent;
    protected VertexElement vertex;
    protected String id;
    protected Double capacity;
    protected Double reliability;

    public Server(VertexElement vertexParent, VertexElement vertex, Double capacity) {
        this.id = vertex.getVertexID();
        this.vertexParent = vertexParent;
        this.vertex = vertex;
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

    public VertexElement getVertex() {
        return vertex;
    }
}
