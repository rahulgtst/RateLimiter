package app.vercel.rahulgtst.strategies;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import app.vercel.rahulgtst.entities.FixedWindow;

public class FixedWindowStrategy<K> implements RateLimiterStrategy<K> {
    private final ConcurrentHashMap<K, FixedWindow> store;
    private final long MAX_LIMIT;
    private final long DURATION;

    public FixedWindowStrategy(long max_limit, long duration) {
        MAX_LIMIT = max_limit;
        DURATION = duration;
        store = new ConcurrentHashMap<>();
    }

    @Override
    public boolean allow(K key) {
        long now = System.currentTimeMillis();
        AtomicBoolean allowed = new AtomicBoolean(true);

        store.compute(key, (id, window) -> {
            // First request for user
            if (window == null) {
                return new FixedWindow(1, now);
            }

            // Window expired → reset
            if (now - window.getTimestamp() > DURATION * 1000) {
                window.setCount(1);
                window.setTimestamp(now);
                return window;
            }

            // Limit exceeded
            if (window.getCount() >= MAX_LIMIT) {
                allowed.set(false);
                return window;
            }

            // Increment count
            window.setCount(window.getCount() + 1);
            return window;
        });
        return allowed.get();
    }
}
