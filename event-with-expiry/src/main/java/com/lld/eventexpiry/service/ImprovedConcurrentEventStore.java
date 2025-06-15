package com.lld.eventexpiry.service;

import com.lld.eventexpiry.model.Event;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

@Slf4j
public class ImprovedConcurrentEventStore implements EventStore {
    private final ConcurrentHashMap<String, ConcurrentSkipListSet<Event>> eventMap;
    private final ConcurrentSkipListSet<Event> expirySet;
    
    public ImprovedConcurrentEventStore() {
        this.eventMap = new ConcurrentHashMap<>();
        this.expirySet = new ConcurrentSkipListSet<>(
            Comparator.comparing(Event::getExpiryTime)
                .thenComparing(Event::getId)
                .thenComparing(System::identityHashCode)
        );
    }

    @Override
    public void addEvent(Event event) {
        // Add to expiry set first (atomic)
        expirySet.add(event);
        
        // Add to event map using merge for atomic update
        eventMap.merge(event.getId(), 
            new ConcurrentSkipListSet<>(Set.of(event)),
            (existingSet, newSet) -> {
                existingSet.add(event);
                return existingSet;
            });
    }

    @Override
    public List<Event> getUnexpiredEvents(String id) {
        ConcurrentSkipListSet<Event> events = eventMap.get(id);
        if (events == null) return List.of();
        
        Instant now = Instant.now();
        return events.headSet(new Event(id, -1, now, null), true)
                   .stream()
                   .filter(e -> e.getId().equals(id)) // Needed because of shared comparator
                   .collect(Collectors.toList());
    }

    @Override
    public void removeExpiredEvents() {
        Instant now = Instant.now();
        NavigableSet<Event> expired = expirySet.headSet(new Event("",-1,  now, null), true);
        
        // Batch processing to minimize lock contention
        expired.forEach(event -> {
            // Remove from expiry set first
            if (expirySet.remove(event)) {
                // Then remove from event map
                eventMap.computeIfPresent(event.getId(), (id, set) -> {
                    set.remove(event);
                    return set.isEmpty() ? null : set;
                });
            }
        });
    }
}