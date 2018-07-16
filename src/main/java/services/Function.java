package services;

public class Function {

    private int type;
    private boolean replicable;
    private double load;

    public Function() {
    }

    public Function(int type, boolean replicable, double load) {
        this.type = type;
        this.replicable = replicable;
        this.load = load;
    }

    public int getType() {
        return type;
    }

    public boolean isReplicable() {
        return replicable;
    }

    public double getLoad() {
        return load;
    }
}
