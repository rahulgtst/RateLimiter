package app.vercel.rahulgtst.strategies;

import app.vercel.rahulgtst.entities.SlidingWindow;

import java.util.ArrayDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SlidingWindowStrategy<K> implements RateLimiterStrategy<K> {
    private final ConcurrentHashMap<K, SlidingWindow> store;
    private final long MAX_LIMIT;
    private final long DURATION;

    public SlidingWindowStrategy(long MAX_LIMIT, long DURATION) {
        this.MAX_LIMIT = MAX_LIMIT;
        this.DURATION = DURATION;
        this.store = new ConcurrentHashMap<>();
    }

    @Override
    public boolean allow(K key) {
        long now = System.currentTimeMillis();

        AtomicBoolean allowed = new AtomicBoolean(true);

        store.compute(key, (id, window) -> {
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
                allowed.set(false);
                return window;
            }

            deque.addLast(now);
            return window;
        });

        return allowed.get();
    }
}
