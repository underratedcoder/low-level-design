package com.lld.ratelimiter.service.impl;

import com.lld.ratelimiter.model.UserRequest;
import com.lld.ratelimiter.service.IRateLimiter;
import com.lld.ratelimiter.service.RateLimiter;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LeakyBucketRateLimiter extends RateLimiter implements IRateLimiter {
    private final int bucketSize;
    private final int leakRatePerWindow; // Number of requests to leak per windowSize
    private final Map<String, Queue<UserRequest>> userBuckets = new ConcurrentHashMap<>();
    private final Map<String, Integer> userCredits = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler;

    public LeakyBucketRateLimiter(int timeWindowLength, TimeUnit timeUnit, int bucketSize, int leakRatePerWindow) {
        super(timeWindowLength, timeUnit);

        this.bucketSize = bucketSize;
        this.leakRatePerWindow = leakRatePerWindow;

        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::leakAll, 0, timeWindowLength, timeUnit);
    }

    @Override
    public synchronized boolean isAllowed(UserRequest request) {
        String userId = request.getUserId();
        userBuckets.putIfAbsent(userId, new LinkedList<>());
        Queue<UserRequest> bucket = userBuckets.get(userId);
        if (bucket.size() < bucketSize) {
            bucket.offer(request);
            return true;
        } else if (userCredits.get(userId) > 0) {
            userCredits.put(userId, userCredits.get(userId) - 1);
            System.out.println("Using credit for user: " + userId);
            return true;
        }
        return false;
    }

    private synchronized void leakAll() {
        for (Map.Entry<String, Queue<UserRequest>> entry : userBuckets.entrySet()) {
            Queue<UserRequest> bucket = entry.getValue();
            int leaked = 0;
            for (int i = 0; i < leakRatePerWindow && bucket.size() > 0; i++) {
                UserRequest req = bucket.poll();
                System.out.println("Leaked request for user: " + req.getUserId());
                leaked++;
            }
            int unused = leakRatePerWindow - leaked;
            if (unused > 0) {
                userCredits.put(entry.getKey(), userCredits.getOrDefault(entry.getKey(), 0) + unused);
                System.out.println("Credited " + unused + " unused leaks to user: " + entry.getKey());
            }
        }
    }

    private void processRequest(UserRequest request) {
        System.out.println("Leaked (processed) request for user: " + request.getUserId() +
                " at " + System.currentTimeMillis());
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }
}
