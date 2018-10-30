package manager.elements;

public class Function {

    private int type;
    private boolean replicable;
    private double load;
    private int maxShareable;
    private int delay;

    public Function() {
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

    public int getDelay() {
        return delay;
    }
}
