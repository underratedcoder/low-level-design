package com.lld.ratelimiter;

import com.lld.ratelimiter.model.UserRequest;
import com.lld.ratelimiter.service.IRateLimiter;
import com.lld.ratelimiter.service.impl.LeakyBucketRateLimiter;

public class RequestDispatcher {
    private final IRateLimiter rateLimiter;

    public RequestDispatcher(IRateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    public void handleRequest(UserRequest request) {
        if (rateLimiter.isAllowed(request)) {
            System.out.println("Request accepted for user: " + request.getUserId());
            // For token/fixed/sliding windows, call process explicitly
            if (!(rateLimiter instanceof LeakyBucketRateLimiter)) {
                processRequest(request);
            }
        } else {
            System.out.println("Request rejected for user: " + request.getUserId());
        }
    }

    private void processRequest(UserRequest request) {
        System.out.println("Processing request for user: " + request.getUserId());
    }
}
