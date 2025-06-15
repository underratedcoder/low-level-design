package com.lld.eventexpiry;

import com.lld.eventexpiry.model.Event;
import com.lld.eventexpiry.service.ConcurrentEventStore;
import com.lld.eventexpiry.service.EventStore;
import com.lld.eventexpiry.service.ExpiryManager;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventSystemDemo {
    public static void main(String[] args) throws InterruptedException {
        // Create event store
        EventStore eventStore = new ConcurrentEventStore();
        
        // Start expiry manager (cleanup every second)
        ExpiryManager expiryManager = new ExpiryManager(eventStore, Duration.ofSeconds(1));
        expiryManager.start();
        
        // Simulate concurrent event ingestion
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        // Add events for ID1 (some expired, some not)
        executor.submit(() -> {
            eventStore.addEvent(new Event("ID1",1,
                Instant.now().plus(5, ChronoUnit.SECONDS), "Event1"));
            eventStore.addEvent(new Event("ID1", 2,
                Instant.now().minus(1, ChronoUnit.SECONDS), "Event2"));
            eventStore.addEvent(new Event("ID1", 3,
                Instant.now().plus(10, ChronoUnit.SECONDS), "Event3"));
        });
        
        // Add events for ID2
        executor.submit(() -> {
            eventStore.addEvent(new Event("ID2", 3,
                Instant.now().plus(3, ChronoUnit.SECONDS), "EventA"));
            eventStore.addEvent(new Event("ID2", 4,
                Instant.now().plus(7, ChronoUnit.SECONDS), "EventB"));
        });
        
        // Wait for events to be processed
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
        
        // Query events
        System.out.println("Unexpired events for ID1:");
        List<Event> id1Events = eventStore.getUnexpiredEvents("ID1");
        id1Events.forEach(System.out::println);
        
        System.out.println("\nUnexpired events for ID2:");
        List<Event> id2Events = eventStore.getUnexpiredEvents("ID2");
        id2Events.forEach(System.out::println);
        
        // Wait and show cleanup
        Thread.sleep(2000);
        System.out.println("\nAfter 2 seconds, unexpired events for ID1:");
        eventStore.getUnexpiredEvents("ID1").forEach(System.out::println);
        
        // Shutdown
        expiryManager.stop();
    }
}