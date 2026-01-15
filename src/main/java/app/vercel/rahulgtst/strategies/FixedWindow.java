package app.vercel.rahulgtst.strategies;

import java.util.concurrent.ConcurrentHashMap;
import app.vercel.rahulgtst.entities.Window;
import app.vercel.rahulgtst.entities.Request;

public class FixedWindow implements RateLimiterStrategy {
    private final ConcurrentHashMap<String, Window> store;
    private final long MAX_LIMIT;
    private final long DURATION;

    public FixedWindow(long max_limit, long duration) {
        MAX_LIMIT=max_limit;
        DURATION=duration;
        store = new ConcurrentHashMap<>();
    }

    @Override
    public boolean check(Request req) {
        String userId = req.getUserId();
        Window window = store.get(userId);
        long now = System.currentTimeMillis();

        // First request for user
        if (window == null) {
            store.put(userId, new Window(1, now));
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
