package com.lld.hitcounter;

import java.util.concurrent.ConcurrentHashMap;

public class HighScaleHitCounter {
    private final int windowSize;
    private final ConcurrentHashMap<String, SlidingWindowCounter> keyCounters = new ConcurrentHashMap<>();

    public HighScaleHitCounter(int windowSize) {
        this.windowSize = windowSize;
    }

    public void count(String key, long t) {
        SlidingWindowCounter counter = keyCounters.computeIfAbsent(key, k -> new SlidingWindowCounter(windowSize));
        counter.count(t);
    }

    public int getCount(String key, long t) {
        SlidingWindowCounter counter = keyCounters.get(key);
        return counter == null ? 0 : counter.getCount(t);
    }

    public int getAllCount(long t) {
        return keyCounters.values().stream()
                .mapToInt(counter -> counter.getCount(t))
                .sum();
    }
}
