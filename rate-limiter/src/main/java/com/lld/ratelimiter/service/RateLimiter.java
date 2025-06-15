package com.lld.ratelimiter.service;

import java.util.concurrent.TimeUnit;

public abstract class RateLimiter {
    protected int timeWindowLength;
    protected TimeUnit timeUnit;

    public RateLimiter(int timeWindowLength, TimeUnit timeUnit) {
        this.timeWindowLength = timeWindowLength;
        this.timeUnit = timeUnit;
    }
}
