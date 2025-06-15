package com.lld.ratelimiter.util;

public class TimeUtil {
    public static long currTimeInSec() {
        return System.currentTimeMillis() / 1000;
    }
}
