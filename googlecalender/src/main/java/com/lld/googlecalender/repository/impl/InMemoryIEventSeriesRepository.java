package com.lld.googlecalender.repository.impl;

import com.lld.googlecalender.model.EventSeries;
import com.lld.googlecalender.repository.IEventSeriesRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryIEventSeriesRepository implements IEventSeriesRepository {
    private final Map<UUID, EventSeries> series = new ConcurrentHashMap<>();

    @Override
    public EventSeries save(EventSeries eventSeries) {
        if (eventSeries.getId() == null) {
            eventSeries.setId(UUID.randomUUID());
        }
        series.put(eventSeries.getId(), eventSeries);
        return eventSeries;
    }

    @Override
    public Optional<EventSeries> findById(UUID id) {
        return Optional.ofNullable(series.get(id));
    }

    @Override
    public List<EventSeries> findByCreatedBy(UUID userId) {
        return series.values().stream()
                .filter(eventSeries -> userId.equals(eventSeries.getCreatedBy()))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        series.remove(id);
    }
}