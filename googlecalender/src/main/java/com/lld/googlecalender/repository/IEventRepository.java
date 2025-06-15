package com.lld.googlecalender.repository;

import com.lld.googlecalender.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IEventRepository {
    Event save(Event event);
    Optional<Event> findById(UUID id);
    List<Event> findByEventIds(List<UUID> eventIds);
    List<Event> findBySeriesId(UUID seriesId);
    List<Event> findByTimeRange(LocalDateTime start, LocalDateTime end);
    List<Event> findAll();
    void delete(UUID id);
}