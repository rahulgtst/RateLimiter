package app.vercel.rahulgtst.entities;

public class Window {
    private long count;
    private long timestamp;

    public Window(long count, long timestamp) {
        this.count = count;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
