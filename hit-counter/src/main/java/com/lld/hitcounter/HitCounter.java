package com.lld.hitcounter;

import java.util.*;
import java.util.concurrent.*;

public class HitCounter {
    private final int WINDOW_SIZE_SECONDS;
    
    // Map to store per-key timestamps
    private final ConcurrentHashMap<String, Deque<Long>> hitsMap = new ConcurrentHashMap<>();

    // Lock map for per-key synchronization
    private final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();

    public HitCounter(int windowSizeSeconds) {
        this.WINDOW_SIZE_SECONDS = windowSizeSeconds;
    }

    // Increment count for a key at time t (in seconds)
    public void count(String key, long t) {
        // Get or create the deque and lock
        Deque<Long> queue = hitsMap.computeIfAbsent(key, k -> new ArrayDeque<>());
        Object lock = locks.computeIfAbsent(key, k -> new Object());

        synchronized (lock) {
            purgeOld(queue, t);
            queue.offerLast(t);
        }
    }

    // Get count for a specific key at time t (in seconds)
    public int getCount(String key, long t) {
        Deque<Long> queue = hitsMap.get(key);
        if (queue == null) return 0;

        Object lock = locks.get(key);
        synchronized (lock) {
            purgeOld(queue, t);
            return queue.size();
        }
    }

    // Get total count of all keys at time t (in seconds)
    public int getAllCount(long t) {
        int total = 0;

        for (Map.Entry<String, Deque<Long>> entry : hitsMap.entrySet()) {
            String key = entry.getKey();
            Deque<Long> queue = entry.getValue();
            Object lock = locks.get(key);

            synchronized (lock) {
                purgeOld(queue, t);
                total += queue.size();
            }
        }

        return total;
    }

    // Helper method to remove timestamps outside the time window
    private void purgeOld(Deque<Long> queue, long currentTime) {
        while (!queue.isEmpty() && queue.peekFirst() <= currentTime - WINDOW_SIZE_SECONDS) {
            queue.pollFirst();
        }
    }
}
