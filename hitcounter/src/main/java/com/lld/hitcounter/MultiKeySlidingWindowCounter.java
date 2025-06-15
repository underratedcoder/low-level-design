package com.lld.hitcounter;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiKeySlidingWindowCounter {

    private static class Bucket {
        volatile long timestamp;
        final AtomicInteger count = new AtomicInteger();
    }

    private static class SlidingWindowCounter {
        private final int windowSize;
        private final Bucket[] buckets;

        public SlidingWindowCounter(int windowSize) {
            this.windowSize = windowSize;
            this.buckets = new Bucket[windowSize];
            for (int i = 0; i < windowSize; i++) {
                buckets[i] = new Bucket();
            }
        }

        public void count(long t) {
            int index = (int) (t % windowSize);
            Bucket bucket = buckets[index];

            synchronized (bucket) {
                if (bucket.timestamp != t) {
                    bucket.timestamp = t;
                    bucket.count.set(0);
                }
                bucket.count.incrementAndGet();
            }
        }

        public int getCount(long t) {
            int sum = 0;
            for (Bucket bucket : buckets) {
                if (t - bucket.timestamp < windowSize) {
                    sum += bucket.count.get();
                }
            }
            return sum;
        }
    }

    private final int windowSize;
    private final ConcurrentHashMap<String, SlidingWindowCounter> counterMap;

    public MultiKeySlidingWindowCounter(int windowSize) {
        this.windowSize = windowSize;
        this.counterMap = new ConcurrentHashMap<>();
    }

    public void count(String key, long t) {
        SlidingWindowCounter counter = counterMap.computeIfAbsent(key, k -> new SlidingWindowCounter(windowSize));
        counter.count(t);
    }

    public int getCount(String key, long t) {
        SlidingWindowCounter counter = counterMap.get(key);
        return (counter != null) ? counter.getCount(t) : 0;
    }

    public int getAllCount(long t) {
        int total = 0;
        for (Map.Entry<String, SlidingWindowCounter> entry : counterMap.entrySet()) {
            total += entry.getValue().getCount(t);
        }
        return total;
    }
}
