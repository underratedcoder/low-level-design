package com.lld.ratelimiter.service.impl;

import com.lld.ratelimiter.model.UserRequest;
import com.lld.ratelimiter.service.IRateLimiter;
import com.lld.ratelimiter.service.RateLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TokenBucketRateLimiter extends RateLimiter implements IRateLimiter {
    private final int bucketSize;
    private final int refillSize;

    private final Map<String, Integer> userTokens = new ConcurrentHashMap<>();
    // TODO - Credits
/*
    private final Map<String, Integer> userCredits = new ConcurrentHashMap<>();
*/

    private final ScheduledExecutorService scheduler;

    public TokenBucketRateLimiter(int timeWindowLength, TimeUnit timeUnit, int bucketSize, int refillSize) {
        super(timeWindowLength, timeUnit);
        this.bucketSize = bucketSize;
        this.refillSize = refillSize;

        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::refillAll, 0, timeWindowLength, timeUnit);
    }

    // TODO - Think about the approach to refill lazily
    @Override
    public synchronized boolean isAllowed(UserRequest request) {
        userTokens.putIfAbsent(request.getUserId(), refillSize);
        int tokens = userTokens.get(request.getUserId());
        if (tokens > 0) {
            tokens--;
            userTokens.put(request.getUserId(), tokens);
            return true;
        }
        // TODO - Credits
/*
        else if (userCredits.getOrDefault(request.getUserId(), 0) > 0) {
            int credits = userCredits.getOrDefault(request.getUserId(), 0);
            credits--;
            userCredits.put(request.getUserId(), credits);
            return true;
        }
*/
        return false;
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }

    private synchronized void refillAll() {
        for (String user : userTokens.keySet()) {
            int current = userTokens.get(user);
            if (current + refillSize <= bucketSize) {
                userTokens.put(user, current + refillSize);
            } else {
                userTokens.put(user, bucketSize);
            // TODO - Credits
/*
                int newCredits = current + refillSize - bucketSize;
                int oldCredit = userCredits.getOrDefault(user, 0);
                userCredits.put(user, oldCredit + newCredits);
*/
            }
        }
    }
}