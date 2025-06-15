package com.lld.ratelimiter.service.impl;

import com.lld.ratelimiter.model.UserRequest;
import com.lld.ratelimiter.service.IRateLimiter;
import com.lld.ratelimiter.service.RateLimiter;
import com.lld.ratelimiter.util.TimeUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class FixedWindowRateLimiter extends RateLimiter implements IRateLimiter {
    private final int maxAllowedRequests;
    private final Map<String, Long> userWindowStart = new ConcurrentHashMap<>();
    private final Map<String, Integer> userRequestCounts = new ConcurrentHashMap<>();
    private final Map<String, Integer> userCredits = new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(int timeWindowLength, TimeUnit timeUnit, int maxAllowedRequests) {
        super(timeWindowLength, timeUnit);
        this.maxAllowedRequests = maxAllowedRequests;
    }

    @Override
    public synchronized boolean isAllowed(UserRequest request) {
        long currentTime = TimeUtil.currTimeInSec();
        userWindowStart.putIfAbsent(request.getUserId(), currentTime);
        userRequestCounts.putIfAbsent(request.getUserId(), 0);

        long windowStart = userWindowStart.get(request.getUserId());

        if (currentTime - windowStart >= timeWindowLength) {
            int countCount = userRequestCounts.get(request.getUserId());
            int credits = Math.max(maxAllowedRequests - countCount, 0);
            userCredits.put(request.getUserId(), userCredits.getOrDefault(request.getUserId(), 0) + credits);

            userWindowStart.put(request.getUserId(), currentTime);
            userRequestCounts.put(request.getUserId(), 0);
        }

        int count = userRequestCounts.get(request.getUserId());

        if (count < maxAllowedRequests) {
            userRequestCounts.put(request.getUserId(), count + 1);
            return true;
        }
        else if (userCredits.get(request.getUserId()) > 0) {
            userCredits.put(request.getUserId(), userCredits.get(request.getUserId()) - 1);
            return true;
        }

        return false;
    }
}
