package app.vercel.rahulgtst.entities;

public class LeakyBucket {
    private double current;
    private long timestamp;

    public LeakyBucket(double current, long timestamp) {
        this.current = current;
        this.timestamp = timestamp;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
