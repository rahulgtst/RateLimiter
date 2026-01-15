package app.vercel.rahulgtst.strategies;

import app.vercel.rahulgtst.entities.Request;

public interface RateLimiterStrategy {
    public abstract boolean check(Request req);
}
