package app.vercel.rahulgtst.strategies;

import app.vercel.rahulgtst.entities.TokenBucket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class TokenBucketStrategy<K> implements RateLimiterStrategy<K> {
    private final double REFILL_RATE; // Refill Rate Per Second
    private final double CAPACITY;
    private final ConcurrentHashMap<K, TokenBucket> store;

    public TokenBucketStrategy(double REFILL_RATE, double CAPACITY) {
        this.REFILL_RATE = REFILL_RATE;
        this.CAPACITY = CAPACITY;
        store = new ConcurrentHashMap<>();
    }

    @Override
    public boolean allow(K key) {
        long now = System.currentTimeMillis();

        AtomicBoolean allowed = new AtomicBoolean(true);

        store.compute(key, (id, bucket) -> {
            if (bucket == null) {
                TokenBucket newBucket = new TokenBucket(CAPACITY, now);
                newBucket.setTokens(newBucket.getTokens() - 1);
                return newBucket;
            }


            long timestamp = bucket.getTimestamp();
            double addTokens = ((now-timestamp)/1000.0)*REFILL_RATE;
            bucket.setTokens(Math.min(CAPACITY, bucket.getTokens()+addTokens));
            bucket.setTimestamp(now);

            if (bucket.getTokens() < 1) {
                allowed.set(false);
                return bucket;
            }

            bucket.setTokens(bucket.getTokens()-1);
            return bucket;
        });

        return allowed.get();
    }
}
