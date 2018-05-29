package services;

public class Function {

    private String id;
    private boolean replicable;
    private double load;

    public Function() {
    }

    public Function(String id, boolean replicable, double load) {
        this.id = id;
        this.replicable = replicable;
        this.load = load;
    }

    public String getId() {
        return id;
    }

    public boolean isReplicable() {
        return replicable;
    }

    public double getLoad() {
        return load;
    }
}
