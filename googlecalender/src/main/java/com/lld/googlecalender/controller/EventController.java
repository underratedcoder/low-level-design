package com.lld.googlecalender.controller;

import com.lld.googlecalender.dto.CreateEventRequest;
import com.lld.googlecalender.dto.CreateEventSeriesRequest;
import com.lld.googlecalender.dto.UpdateEventRequest;
import com.lld.googlecalender.model.Event;
import com.lld.googlecalender.model.EventSeries;
import com.lld.googlecalender.service.ICalenderService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class EventController {
    private final ICalenderService ICalenderService;
    
    public Event createEvent(CreateEventRequest request) {
        return ICalenderService.createEvent(
            request.getTitle(),
            request.getDescription(),
            request.getStartTime(),
            request.getEndTime(),
            request.getCreatedBy(),
            request.getCalendarId(),
            request.getParticipantIds()
        );
    }
    
    public EventSeries createEventSeries(CreateEventSeriesRequest request) {
        return ICalenderService.createEventSeries(
            request.getTitle(),
            request.getDescription(),
            request.getStartTime(),
            request.getEndTime(),
            request.getRecurrenceType(),
            request.getRecurrenceInterval(),
            request.getSeriesEndDate(),
            request.getCreatedBy(),
            request.getCalendarId(),
            request.getParticipantIds()
        );
    }
    
    public Event getEvent(UUID eventId) {
        return ICalenderService.getEventById(eventId);
    }
    
    public List<Event> getEventsBySeries(UUID seriesId) {
        return ICalenderService.getEventsBySeriesId(seriesId);
    }
    
    public List<Event> getEventsByCalendar(UUID calendarId) {
        return ICalenderService.getEventsByCalendarId(calendarId);
    }
    
    public List<Event> getEventsByTimeRange(LocalDateTime start, LocalDateTime end) {
        return ICalenderService.getEventsByTimeRange(start, end);
    }
    
    public List<Event> getDailyEvents(UUID userId, LocalDateTime day) {
        return ICalenderService.getEventsByDay(userId, day);
    }
    
    public Event updateEvent(UUID eventId, UpdateEventRequest request) {
        Event event = ICalenderService.getEventById(eventId);
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());
        
        return ICalenderService.updateEvent(event);
    }
    
    public List<Event> updateEventSeries(UUID seriesId, String title, String description) {
        return ICalenderService.updateEventSeries(seriesId, title, description);
    }
    
    public void deleteEvent(UUID eventId) {
        ICalenderService.deleteEvent(eventId);
    }
    
    public void deleteEventSeries(UUID seriesId) {
        ICalenderService.deleteEventSeries(seriesId);
    }
}