package com.lld.googlecalender.repository.impl;

import com.lld.googlecalender.enums.EventStatus;
import com.lld.googlecalender.model.Event;
import com.lld.googlecalender.repository.IEventRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryIEventRepository implements IEventRepository {
    private final Map<UUID, Event> events = new ConcurrentHashMap<>();

    @Override
    public Event save(Event event) {
        if (event.getId() == null) {
            event.setId(UUID.randomUUID());
        }
        events.put(event.getId(), event);
        return event;
    }

    @Override
    public Optional<Event> findById(UUID id) {
        return Optional.ofNullable(events.get(id));
    }

    @Override
    public List<Event> findByEventIds(List<UUID> eventIds) {
        Set<UUID> eventIdsSet = new HashSet<>(eventIds);
        return events.values().stream()
                .filter(event -> eventIdsSet.contains(event.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findBySeriesId(UUID seriesId) {
        return events.values().stream()
                .filter(event -> seriesId != null && seriesId.equals(event.getSeriesId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findByTimeRange(LocalDateTime start, LocalDateTime end) {
        return events.values().stream()
                .filter(event -> 
                    (event.getStartTime().isEqual(start) || event.getStartTime().isAfter(start)) &&
                    (event.getStartTime().isBefore(end)) &&
                    (event.getStatus() != EventStatus.CANCELLED))
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findAll() {
        return new ArrayList<>(events.values());
    }

    @Override
    public void delete(UUID id) {
        events.remove(id);
    }
}