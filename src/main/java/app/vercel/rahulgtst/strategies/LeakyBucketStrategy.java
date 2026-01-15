package app.vercel.rahulgtst.strategies;

import app.vercel.rahulgtst.entities.LeakyBucket;
import app.vercel.rahulgtst.entities.Request;

import java.util.concurrent.ConcurrentHashMap;

public class LeakyBucketStrategy implements RateLimiterStrategy {
    private final double CAPACITY;
    private final double LEAK_RATE; // leak rate per second
    private final ConcurrentHashMap<String, LeakyBucket> store;

    public LeakyBucketStrategy(double LEAK_RATE, double CAPACITY) {
        this.CAPACITY = CAPACITY;
        this.LEAK_RATE = LEAK_RATE;
        store = new ConcurrentHashMap<>();
    }

    @Override
    public boolean check(Request req) {
        String userId = req.getUserId();
        long now = System.currentTimeMillis();
        final boolean[] allowed = {true};

        store.compute(userId, (key, bucket) -> {
            if(bucket == null) {
                bucket = new LeakyBucket(0, now);
            }

            double elapsedTime = (now-bucket.getTimestamp())/1000.0;
            double current = Math.max(0, bucket.getCurrent()-elapsedTime*LEAK_RATE);
            bucket.setCurrent(current);
            bucket.setTimestamp(now);

            if(bucket.getCurrent()+1 > CAPACITY) {
                allowed[0] = false;
                return bucket;
            }

            bucket.setCurrent(current+1);
            return bucket;
        });
        return allowed[0];
    }
}
