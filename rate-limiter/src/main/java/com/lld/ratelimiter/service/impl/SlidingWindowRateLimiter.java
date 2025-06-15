package com.lld.ratelimiter.service.impl;

import com.lld.ratelimiter.model.UserRequest;
import com.lld.ratelimiter.service.IRateLimiter;
import com.lld.ratelimiter.service.RateLimiter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class SlidingWindowRateLimiter extends RateLimiter implements IRateLimiter {
    private final int maxAllowedRequests;
    private final Map<String, Deque<Long>> userRequestLogs = new ConcurrentHashMap<>();

    public SlidingWindowRateLimiter(int timeWindowLength, TimeUnit timeUnit, int maxAllowedRequests) {
        super(timeWindowLength, timeUnit);
        this.maxAllowedRequests = maxAllowedRequests;
    }

    @Override
    public synchronized boolean isAllowed(UserRequest request) {
        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime - timeUnit.toMillis(timeWindowLength);

        userRequestLogs.putIfAbsent(request.getUserId(), new ArrayDeque<>());
        Deque<Long> timestamps = userRequestLogs.get(request.getUserId());

        while (!timestamps.isEmpty() && timestamps.peekFirst() < windowStart) {
            timestamps.pollFirst();
        }

        if (timestamps.size() < maxAllowedRequests) {
            timestamps.offerLast(currentTime);
            return true;
        }
        return false;
    }

    public void shutdown() {}
}
