package com.lld.ratelimiter.service.impl;

import com.lld.ratelimiter.model.UserRequest;
import com.lld.ratelimiter.service.IRateLimiter;
import com.lld.ratelimiter.service.RateLimiter;
import com.lld.ratelimiter.util.TimeUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class SlidingWindowRateLimiter extends RateLimiter implements IRateLimiter {
    private final int maxAllowedRequests;
    private final Map<String, Queue<Long>> userRequestLogs = new ConcurrentHashMap<>();

    public SlidingWindowRateLimiter(int timeWindowLength, TimeUnit timeUnit, int maxAllowedRequests) {
        super(timeWindowLength, timeUnit);
        this.maxAllowedRequests = maxAllowedRequests;
    }

    @Override
    public synchronized boolean isAllowed(UserRequest request) {
        userRequestLogs.putIfAbsent(request.getUserId(), new LinkedList<>());

        long currentTime = TimeUtil.currTimeInSec();
        long windowStart = currentTime - timeWindowLength;

        Queue<Long> timestamps = userRequestLogs.get(request.getUserId());

        while (!timestamps.isEmpty() && timestamps.peek() < windowStart) {
            timestamps.poll();
        }

        if (timestamps.size() < maxAllowedRequests) {
            timestamps.offer(currentTime);
            return true;
        }

        return false;
    }

    public void shutdown() {}
}
