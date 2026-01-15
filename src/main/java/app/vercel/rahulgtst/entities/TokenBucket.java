package app.vercel.rahulgtst.entities;

public class TokenBucket {
    private double tokens;
    private long timestamp;

    public TokenBucket(double tokens, long timestamp) {
        this.tokens = tokens;
        this.timestamp = timestamp;
    }

    public void setTokens(double tokens) {
        this.tokens = tokens;
    }

    public double getTokens() {
        return tokens;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
