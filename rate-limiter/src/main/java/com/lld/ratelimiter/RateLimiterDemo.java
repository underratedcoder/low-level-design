package com.lld.ratelimiter;

import com.lld.ratelimiter.enums.RateLimiterType;
import com.lld.ratelimiter.factory.RateLimiterFactory;
import com.lld.ratelimiter.model.UserRequest;
import com.lld.ratelimiter.service.IRateLimiter;
import com.lld.ratelimiter.service.impl.LeakyBucketRateLimiter;
import com.lld.ratelimiter.service.impl.TokenBucketRateLimiter;
import com.lld.ratelimiter.util.TimeUtil;

import java.util.concurrent.TimeUnit;

public class RateLimiterDemo {
    public static void main(String[] args) throws InterruptedException {
        IRateLimiter limiter = RateLimiterFactory.createBucketRateLimiter(
                RateLimiterType.LEAKY_BUCKET, 5, TimeUnit.SECONDS, 5, -1, 3
        );

        RequestDispatcher dispatcher = new RequestDispatcher(limiter);

        for (int i = 1; i <= 10; i++) {
            dispatcher.handleRequest(new UserRequest(i, "user-1", TimeUtil.currTimeInSec()));
            //Thread.sleep(100); // simulate 100ms between requests
        }

        for (int i = 1; i <= 10; i++) {
            dispatcher.handleRequest(new UserRequest(i, "user-2", TimeUtil.currTimeInSec()));
            //Thread.sleep(100); // simulate 100ms between requests
        }

        Thread.sleep(6000);

        for (int i = 1; i <= 5; i++) {
            dispatcher.handleRequest(new UserRequest(i, "user-2", TimeUtil.currTimeInSec()));
            //Thread.sleep(100); // simulate 100ms between requests
        }

        for (int i = 1; i <= 5; i++) {
            dispatcher.handleRequest(new UserRequest(i, "user-1", TimeUtil.currTimeInSec()));
            //Thread.sleep(100); // simulate 100ms between requests
        }

        // Give time for leaky bucket to process
        Thread.sleep(25000);

        // Shutdown if needed
        if (limiter instanceof TokenBucketRateLimiter) {
            ((TokenBucketRateLimiter) limiter).shutdown();
        }
        if (limiter instanceof LeakyBucketRateLimiter) {
            ((LeakyBucketRateLimiter) limiter).shutdown();
        }
    }
}
