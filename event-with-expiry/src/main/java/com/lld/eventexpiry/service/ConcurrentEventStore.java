package com.lld.eventexpiry.service;

import com.lld.eventexpiry.model.Event;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

@Slf4j
public class ConcurrentEventStore implements EventStore {
    // Map of ID to sorted set of events (sorted by expiry time)
    private final ConcurrentHashMap<String, PriorityBlockingQueue<Event>> eventMap;
    
    // Queue for efficient expiry management (ordered by expiry time)
    private final PriorityBlockingQueue<Event> expiryQueue;
    
    public ConcurrentEventStore() {
        this.eventMap = new ConcurrentHashMap<>();
        this.expiryQueue = new PriorityBlockingQueue<>(
            1000, 
            Comparator.comparing(Event::getExpiryTime)
        );
    }

    @Override
    public void addEvent(Event event) {
        if (event == null || event.getId() == null) {
            throw new IllegalArgumentException("Event and ID cannot be null");
        }

        // Add to expiry queue
        expiryQueue.add(event);
        
        // Add to event map
        eventMap.compute(event.getId(), (id, queue) -> {
            if (queue == null) {
                queue = new PriorityBlockingQueue<>(
                    10, 
                    Comparator.comparing(Event::getExpiryTime)
                );
            }
            queue.add(event);
            return queue;
        });
    }

    @Override
    public List<Event> getUnexpiredEvents(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        PriorityBlockingQueue<Event> events = eventMap.get(id);
        if (events == null || events.isEmpty()) {
            return Collections.emptyList();
        }

        Instant now = Instant.now();
        return events.stream()
                .filter(event -> event.getExpiryTime().isAfter(now))
                .collect(Collectors.toList());
    }

    @Override
    public void removeExpiredEvents() {
        Instant now = Instant.now();
        Event event;
        
        // Process all expired events in the queue
        while ((event = expiryQueue.peek()) != null && 
               event.getExpiryTime().isBefore(now)) {
            
            event = expiryQueue.poll();
            if (event == null) {
                break;
            }
            
            // Remove from the event map
            Event finalEvent = event;
            eventMap.computeIfPresent(event.getId(), (id, queue) -> {
                queue.remove(finalEvent);
                return queue.isEmpty() ? null : queue;
            });
        }
    }
}