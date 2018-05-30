package services;

public class Function {

    private int id;
    private boolean replicable;
    private double load;

    public Function() {
    }

    public Function(int id, boolean replicable, double load) {
        this.id = id;
        this.replicable = replicable;
        this.load = load;
    }

    public int getId() {
        return id;
    }

    public boolean isReplicable() {
        return replicable;
    }

    public double getLoad() {
        return load;
    }
}
