package app.vercel.rahulgtst.entities;

import java.util.ArrayDeque;

public class SlidingWindow {
    private final ArrayDeque<Long> timestamps;

    public SlidingWindow(ArrayDeque<Long> timestamps) {
        this.timestamps = timestamps;
    }

    public ArrayDeque<Long> getTimestamps() {
        return timestamps;
    }
}
