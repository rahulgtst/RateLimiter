package app.vercel.rahulgtst.strategies;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import app.vercel.rahulgtst.entities.FixedWindow;
import app.vercel.rahulgtst.entities.Request;

public class FixedWindowStrategy implements RateLimiterStrategy {
    private final ConcurrentHashMap<String, FixedWindow> store;
    private final long MAX_LIMIT;
    private final long DURATION;

    public FixedWindowStrategy(long max_limit, long duration) {
        MAX_LIMIT = max_limit;
        DURATION = duration;
        store = new ConcurrentHashMap<>();
    }

    @Override
    public boolean check(Request req) {
        String userId = req.getUserId();
        long now = System.currentTimeMillis();
        AtomicBoolean allowed = new AtomicBoolean(true);

        store.compute(userId, (key, window) -> {
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
