package com.lld.googlecalender.repository;

import com.lld.googlecalender.model.EventSeries;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IEventSeriesRepository {
    EventSeries save(EventSeries series);
    Optional<EventSeries> findById(UUID id);
    List<EventSeries> findByCreatedBy(UUID userId);
    void delete(UUID id);
}