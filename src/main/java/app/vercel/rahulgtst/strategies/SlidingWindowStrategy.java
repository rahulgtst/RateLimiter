package app.vercel.rahulgtst.strategies;

import app.vercel.rahulgtst.entities.Request;
import app.vercel.rahulgtst.entities.SlidingWindow;

import java.util.ArrayDeque;
import java.util.concurrent.ConcurrentHashMap;

public class SlidingWindowStrategy implements RateLimiterStrategy {
    private final ConcurrentHashMap<String, SlidingWindow> store;
    private final long MAX_LIMIT;
    private final long DURATION;

    public SlidingWindowStrategy(long MAX_LIMIT, long DURATION) {
        this.MAX_LIMIT = MAX_LIMIT;
        this.DURATION = DURATION;
        this.store = new ConcurrentHashMap<>();
    }

    @Override
    public boolean check(Request req) {
        String userId = req.getUserId();
        long now = System.currentTimeMillis();

        final boolean[] allowed = {true};

        store.compute(userId, (key, window) -> {
            if (window == null) {
                ArrayDeque<Long> deque = new ArrayDeque<>();
                deque.addLast(now);
                return new SlidingWindow(deque);
            }

            ArrayDeque<Long> deque = window.getTimestamps();

            // Remove expired timestamps
            while (!deque.isEmpty() && now - deque.peekFirst() > DURATION * 1000) {
                deque.removeFirst();
            }

            if (deque.size() >= MAX_LIMIT) {
                allowed[0] = false;
                return window;
            }

            deque.addLast(now);
            return window;
        });

        return allowed[0];
    }
}
