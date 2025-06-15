package com.lld.googlecalender.service.impl;

import com.lld.googlecalender.enums.RecurrenceType;
import com.lld.googlecalender.model.Calendar;
import com.lld.googlecalender.model.CalenderDateToEventMapping;
import com.lld.googlecalender.model.Event;
import com.lld.googlecalender.model.EventSeries;
import com.lld.googlecalender.repository.ICalendarRepository;
import com.lld.googlecalender.repository.IEventRepository;
import com.lld.googlecalender.repository.IEventSeriesRepository;
import com.lld.googlecalender.service.ICalenderService;
//import com.lld.googlecalender.service.NotificationService;
import com.lld.googlecalender.service.IParticipantService;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CalenderServiceImpl implements ICalenderService {
    private final IEventRepository eventRepository;
    private final IEventSeriesRepository eventSeriesRepository;
    private final IParticipantService IParticipantService;
    private final ICalendarRepository calendarRepository;
//    private final NotificationService notificationService;

    @Override
    public EventSeries createEventSeries(
            String title,
            String description,
            LocalDateTime eventStartTime,
            LocalDateTime eventEndTime,
            UUID createdBy,
            RecurrenceType recurrenceType,
            LocalDateTime seriesEndDate,
            UUID calendarId,
            List<UUID> userIds
    ) {

        // Create the event series
        EventSeries series = EventSeries.builder()
                .id(UUID.randomUUID())
                .title(title)
                .description(description)
                .startDate(eventStartTime)
                .endDate(eventEndTime)
                .createdBy(createdBy)
                .recurrenceType(recurrenceType)
                .seriesEndDate(seriesEndDate)
                .build();

        series = eventSeriesRepository.save(series);

        // Generate all event instances for the series
        List<Event> events = generateEventInstances(series, eventStartTime, eventEndTime, seriesEndDate);

        // Save all event instances
        for (Event event : events) {
            eventRepository.save(event);

            // Add participants to each event instance
            if (userIds != null && !userIds.isEmpty()) {
                for (UUID userId : userIds) {
                    IParticipantService.addParticipant(event.getId(), userId);

                }
            }
        }

        List<Calendar> calendars = new ArrayList<>();

        for (UUID userId : userIds) {
            Calendar calendar = calendarRepository.findByUserId(userId);
            calendars.add(calendar);
        }

        List<CalenderDateToEventMapping> calenderDateToEventMappings = new ArrayList<>();

        for (Event event : events) {
            calenderDateToEventMappings = calendars.stream()
                    .map(c -> CalenderDateToEventMapping.builder()
                            .calendarId(c.getId())
                            .eventDate(event.getStartTime().toLocalDate())
                            .eventId(event.getId())
                            .build())
                    .collect(Collectors.toList());
        }


        calendarRepository.saveCalenderEvents(calenderDateToEventMappings);

        // Notify participants about the series creation
        //notificationService.notifyEventSeriesCreation(series, events);

        return series;
    }

    private List<Event> generateEventInstances(
            EventSeries series,
            LocalDateTime eventStartTime,
            LocalDateTime eventEndTime,
            LocalDateTime seriesEndDate
    ) {

        List<Event> events = new ArrayList<>();

        while (eventStartTime.isBefore(seriesEndDate)) {
            Event event = Event.builder()
                    .id(UUID.randomUUID())
                    .title(series.getTitle())
                    .description(series.getDescription())
                    .startTime(eventStartTime)
                    .endTime(eventEndTime)
                    .seriesId(series.getId())
                    .build();

            events.add(event);

            long plusDays;

            switch (series.getRecurrenceType()) {
                case DAILY:
                    plusDays = 1;
                    break;
                case WEEKLY:
                    plusDays = 7;
                    break;
                case MONTHLY:
                    plusDays = 30;
                    break;
                default:
                    plusDays = Duration.between(eventStartTime, seriesEndDate).toDays();
                    break;
            }

            eventStartTime = eventStartTime.plusDays(plusDays);
            eventStartTime = eventStartTime.plusDays(plusDays);
        }

        return events;
    }

    @Override
    public Event getEventById(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    private List<Event> getEventsBySeriesId(UUID seriesId) {
        return eventRepository.findBySeriesId(seriesId);
    }

    @Override
    public List<Event> getCalendarEvents(UUID calendarId, LocalDate date) {
        List<UUID> eventsId = calendarRepository.fetchCalenderEvents(calendarId, date);
        return eventRepository.findByEventIds(eventsId);
    }

    @Override
    public List<Event> getEventsByTimeRange(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByTimeRange(start, end);
    }

    @Override
    public List<Event> getEventsByDay(UUID userId, LocalDateTime day) {
        LocalDateTime startOfDay = day.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        // Get events in the time range
        List<Event> events = getEventsByTimeRange(startOfDay, endOfDay);

        // Filter events where the user is a participant or creator
        return events.stream()
                .filter(event -> event.getCreatedBy().equals(userId) ||
                               IParticipantService.isUserParticipant(event.getId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public Event updateEvent(Event event) {
        // Verify the event exists
        getEventById(event.getId());

        Event updatedEvent = eventRepository.save(event);

        // Notify participants about the update
        //notificationService.notifyEventUpdate(updatedEvent);

        return updatedEvent;
    }

    @Override
    public List<Event> updateEventSeries(UUID seriesId, String title, String description) {
        // Get the series
        EventSeries series = eventSeriesRepository.findById(seriesId)
                .orElseThrow(() -> new RuntimeException("Event series not found with id: " + seriesId));

        // Update the series
        series.setTitle(title);
        series.setDescription(description);
        eventSeriesRepository.save(series);

        // Get all events in the series
        List<Event> events = getEventsBySeriesId(seriesId);

        // Update all events
        for (Event event : events) {
            event.setTitle(title);
            event.setDescription(description);
            IEventRepository.save(event);
        }

        // Notify participants about the series update
        notificationService.notifyEventSeriesUpdate(series, events);

        return events;
    }

    @Override
    public Event updateEventInSeries(UUID eventId, String title, String description,
                                    LocalDateTime startTime, LocalDateTime endTime) {
        // Get the event
        Event event = getEventById(eventId);

        // Update the event
        event.setTitle(title);
        event.setDescription(description);
        event.setStartTime(startTime);
        event.setEndTime(endTime);

        Event updatedEvent = IEventRepository.save(event);

        // Notify participants about the update
        notificationService.notifyEventUpdate(updatedEvent);

        return updatedEvent;
    }

    @Override
    public void deleteEvent(UUID id) {
        // Verify the event exists
        Event event = getEventById(id);

        // Delete the event
        IEventRepository.delete(id);

        // Notify participants about the cancellation
        notificationService.notifyEventCancellation(event);
    }
}