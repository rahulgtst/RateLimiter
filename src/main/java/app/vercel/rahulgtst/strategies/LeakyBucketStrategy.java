package app.vercel.rahulgtst.strategies;

import app.vercel.rahulgtst.entities.LeakyBucket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class LeakyBucketStrategy<K> implements RateLimiterStrategy<K> {
    private final double CAPACITY;
    private final double LEAK_RATE; // leak rate per second
    private final ConcurrentHashMap<K, LeakyBucket> store;

    public LeakyBucketStrategy(double LEAK_RATE, double CAPACITY) {
        this.CAPACITY = CAPACITY;
        this.LEAK_RATE = LEAK_RATE;
        store = new ConcurrentHashMap<>();
    }

    @Override
    public boolean allow(K key) {
        long now = System.currentTimeMillis();
        AtomicBoolean allowed = new AtomicBoolean(true);

        store.compute(key, (id, bucket) -> {
            if(bucket == null) {
                bucket = new LeakyBucket(0, now);
            }

            double elapsedTime = (now-bucket.getTimestamp())/1000.0;
            double current = Math.max(0, bucket.getCurrent()-elapsedTime*LEAK_RATE);
            bucket.setCurrent(current);
            bucket.setTimestamp(now);

            if(bucket.getCurrent()+1 > CAPACITY) {
                allowed.set(false);
                return bucket;
            }

            bucket.setCurrent(current+1);
            return bucket;
        });
        return allowed.get();
    }
}
