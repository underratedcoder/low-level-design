package com.lld.hitcounter;

public class SlidingWindowCounter {
    private static class Bucket {
        long timestamp;
        int count;
    }

    private final int windowSize;
    private final Bucket[] buckets;

    public SlidingWindowCounter(int windowSizeInSeconds) {
        this.windowSize = windowSizeInSeconds;
        this.buckets = new Bucket[windowSize];
        for (int i = 0; i < windowSize; i++) {
            buckets[i] = new Bucket();
        }
    }

    public synchronized void count(long t) {
        int index = (int) (t % windowSize);
        Bucket bucket = buckets[index];
        if (bucket.timestamp != t) {
            bucket.timestamp = t;
            bucket.count = 0;
        }
        bucket.count++;
    }

    public synchronized int getCount(long t) {
        int total = 0;
        for (Bucket bucket : buckets) {
            if (t - bucket.timestamp < windowSize) {
                total += bucket.count;
            }
        }
        return total;
    }
}