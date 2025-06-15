package com.lld.googlecalender.service;

import com.lld.googlecalender.enums.RecurrenceType;
import com.lld.googlecalender.model.Event;
import com.lld.googlecalender.model.EventSeries;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ICalenderService {
    // Create a recurring event series
    EventSeries createEventSeries(
            String title,
            String description,
            LocalDateTime eventStartTime,
            LocalDateTime eventEndTime,
            UUID createdBy,
            RecurrenceType recurrenceType,
            LocalDateTime seriesEndDate,
            UUID calendarId,
            List<UUID> participantIds
    );
    
    // Get a specific event
    Event getEventById(UUID id);

    // Get events for a calendar
    List<Event> getEventsByCalendarId(UUID calendarId, LocalDate date);
    
    // Get events for a user on a specific day
    List<Event> getEventsByDay(UUID userId, LocalDateTime day);
    
    // Update a single event
    Event updateEvent(Event event);
    
    // Update all events in a series
    List<Event> updateEventSeries(UUID seriesId, String title, String description);
    
    // Update a single event in a series
    Event updateEventInSeries(UUID eventId, String title, String description, 
                             LocalDateTime startTime, LocalDateTime endTime);
}