package com.lld.airline.dao;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

// A custom lock wrapper that tracks user information
public class SeatLockWrapper {
    private final ReentrantLock lock;
    private volatile Long currentLockHolder;
    private volatile Long lockExpiryTime;

    public SeatLockWrapper() {
        this.lock = new ReentrantLock();
        this.currentLockHolder = null;
        this.lockExpiryTime = null;
    }

    public boolean tryLock(Long userId) throws InterruptedException {
        boolean acquired = lock.tryLock(0, TimeUnit.SECONDS);
        if (acquired) {
            this.currentLockHolder = userId;
            this.lockExpiryTime = System.currentTimeMillis() + 60 * 1000;
        }
        return acquired;
    }

    public void lock(Long userId) {
        lock.lock();
        this.currentLockHolder = userId;
        this.lockExpiryTime = System.currentTimeMillis() + 60 * 1000;
    }

    public void unlock() {
        this.currentLockHolder = null;
        this.lockExpiryTime = null;
        lock.unlock();
    }

    public Long getCurrentLockHolder() {
        return currentLockHolder;
    }

    public boolean isLocked() {
        return lock.isLocked();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= lockExpiryTime;
    }
}