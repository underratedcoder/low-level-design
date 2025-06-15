package com.lld.ratelimiter.service;

import com.lld.ratelimiter.model.UserRequest;

public interface IRateLimiter {
    boolean isAllowed(UserRequest request);
}
