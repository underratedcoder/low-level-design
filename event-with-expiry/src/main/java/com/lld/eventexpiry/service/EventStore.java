package com.lld.eventexpiry.service;

import com.lld.eventexpiry.model.Event;

import java.util.List;

public interface EventStore {
    void addEvent(Event event);
    List<Event> getUnexpiredEvents(String id);
    void removeExpiredEvents();
}