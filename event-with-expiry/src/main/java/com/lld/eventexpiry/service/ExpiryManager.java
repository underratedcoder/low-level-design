package com.lld.eventexpiry.service;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExpiryManager {
    private final EventStore eventStore;
    private final ScheduledExecutorService scheduler;
    private final Duration cleanupInterval;

    public ExpiryManager(EventStore eventStore, Duration cleanupInterval) {
        this.eventStore = eventStore;
        this.cleanupInterval = cleanupInterval;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        scheduler.scheduleAtFixedRate(
            () -> {
                try {
                    Instant start = Instant.now();
                    eventStore.removeExpiredEvents();
                    log.debug("Expiry cleanup took {} ms", 
                        Duration.between(start, Instant.now()).toMillis());
                } catch (Exception e) {
                    log.error("Error during expiry cleanup", e);
                }
            },
            0,
            cleanupInterval.toMillis(),
            TimeUnit.MILLISECONDS
        );
    }

    public void stop() {
        scheduler.shutdown();
    }
}