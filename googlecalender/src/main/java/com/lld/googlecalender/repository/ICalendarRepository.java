package com.lld.googlecalender.repository;

import com.lld.googlecalender.model.Calendar;
import com.lld.googlecalender.model.CalenderDateToEventMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ICalendarRepository {
    Calendar save(Calendar calendar);
    Calendar findById(UUID id);
    Calendar findByUserId(UUID userId);
    void saveCalenderEvents(List<CalenderDateToEventMapping> calenderDateToEventMappings);
    List<UUID> fetchCalenderEvents(UUID calenderId, LocalDate localDate);
}