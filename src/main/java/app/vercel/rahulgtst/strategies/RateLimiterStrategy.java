package app.vercel.rahulgtst.strategies;

public interface RateLimiterStrategy<K> {
    boolean allow(K key);
}
