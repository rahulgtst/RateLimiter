package app.vercel.rahulgtst.strategies;

import java.util.concurrent.ConcurrentHashMap;
import app.vercel.rahulgtst.entities.FixedWindow;
import app.vercel.rahulgtst.entities.Request;

public class FixedWindowStrategy implements RateLimiterStrategy {
    private final ConcurrentHashMap<String, FixedWindow> store;
    private final long MAX_LIMIT;
    private final long DURATION;

    public FixedWindowStrategy(long max_limit, long duration) {
        MAX_LIMIT=max_limit;
        DURATION=duration;
        store = new ConcurrentHashMap<>();
    }

    @Override
    public boolean check(Request req) {
        String userId = req.getUserId();
        FixedWindow window = store.get(userId);
        long now = System.currentTimeMillis();

        // First request for user
        if (window == null) {
            store.put(userId, new FixedWindow(1, now));
            return true;
        }

        // Window expired → reset
        if (now - window.getTimestamp() > DURATION * 1000) {
            window.setCount(1);
            window.setTimestamp(now);
            return true;
        }

        // Limit exceeded
        if (window.getCount() >= MAX_LIMIT) {
            return false;
        }

        // Increment count
        window.setCount(window.getCount() + 1);
        return true;
    }
}
