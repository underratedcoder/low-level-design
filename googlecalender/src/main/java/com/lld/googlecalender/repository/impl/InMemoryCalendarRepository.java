package com.lld.googlecalender.repository.impl;

import com.lld.googlecalender.model.Calendar;
import com.lld.googlecalender.repository.ICalendarRepository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCalendarRepository implements ICalendarRepository {

    private final Map<UUID, Calendar> calendars = new ConcurrentHashMap<>();

    @Override
    public Calendar save(Calendar calendar) {
        if (calendar.getId() == null) {
            calendar.setId(UUID.randomUUID());
        }
        calendars.put(calendar.getId(), calendar);
        return calendar;
    }

    @Override
    public Calendar findById(UUID id) {
        return calendars.get(id);
    }

    @Override
    public Calendar findByUserId(UUID userId) {
        return calendars.values().stream()
                .filter(calendar -> userId.equals(calendar.getUserId()))
                .findFirst()
                .get();
    }
}