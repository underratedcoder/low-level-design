package com.lld.ratelimiter.factory;

import com.lld.ratelimiter.enums.RateLimiterType;
import com.lld.ratelimiter.service.*;
import com.lld.ratelimiter.service.impl.FixedWindowRateLimiter;
import com.lld.ratelimiter.service.impl.LeakyBucketRateLimiter;
import com.lld.ratelimiter.service.impl.SlidingWindowRateLimiter;
import com.lld.ratelimiter.service.impl.TokenBucketRateLimiter;

import java.util.concurrent.TimeUnit;

public class RateLimiterFactory {

    public static IRateLimiter createWindowRateLimiter(
            RateLimiterType type,
            int maxAllowedRequests,
            int timeWindowLength,
            TimeUnit timeUnit
    ) {
        switch (type) {
            case FIXED_WINDOW:
                return new FixedWindowRateLimiter(timeWindowLength, timeUnit, maxAllowedRequests);
            case SLIDING_WINDOW:
                return new SlidingWindowRateLimiter(timeWindowLength, timeUnit, maxAllowedRequests);
            default:
                throw new IllegalArgumentException("Unsupported rate limiter type: " + type);
        }
    }

    public static IRateLimiter createBucketRateLimiter(
            RateLimiterType type,
            int bucketSize,
            int timeWindowLength,
            TimeUnit timeUnit,
            int refillSize, // Used only for token bucket
            int leakRatePerWindow // Used only for leaky bucket
    ) {
        switch (type) {
            case TOKEN_BUCKET:
                return new TokenBucketRateLimiter(timeWindowLength, timeUnit, bucketSize, refillSize);
            case LEAKY_BUCKET:
                return new LeakyBucketRateLimiter(timeWindowLength, timeUnit, bucketSize, leakRatePerWindow);
            default:
                throw new IllegalArgumentException("Unsupported rate limiter type: " + type);
        }
    }
}
