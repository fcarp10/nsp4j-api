package services;

public class Function {

    private int type;
    private boolean replicable;
    private double load;
    private int maxShareable;

    public Function() {
    }

    public Function(int type, boolean replicable, double load, int maxShareable) {
        this.type = type;
        this.replicable = replicable;
        this.load = load;
        this.maxShareable = maxShareable;
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

    public int getMaxShareable() {
        return maxShareable;
    }
}
