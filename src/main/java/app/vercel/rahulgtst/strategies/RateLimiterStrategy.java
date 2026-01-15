package app.vercel.rahulgtst.strategies;

import app.vercel.rahulgtst.entities.Request;

public interface RateLimiterStrategy {
    boolean check(Request req);
}
