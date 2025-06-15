package com.lld.googlecalender.service;

import com.lld.googlecalender.enums.ParticipantResponseStatus;
import com.lld.googlecalender.model.Participant;

import java.util.List;
import java.util.UUID;

public interface IParticipantService {
    // Add a participant to an event
    Participant addParticipant(UUID eventId, UUID userId);
    
    // Update a participant's response for a single event
    Participant updateParticipantResponse(UUID eventId, UUID userId, 
                                         ParticipantResponseStatus status);
    
    // Update a participant's response for all events in a series
    List<Participant> updateParticipantResponseForSeries(UUID seriesId, UUID userId, 
                                                       ParticipantResponseStatus status);
    
    // Get all participants for an event
    List<Participant> getParticipantsByEventId(UUID eventId);
    
    // Get all events a user is participating in
    List<Participant> getParticipationsByUserId(UUID userId);
    
    // Remove a participant from an event
    void removeParticipant(UUID eventId, UUID userId);
    
    // Remove a participant from all events in a series
    void removeParticipantFromSeries(UUID seriesId, UUID userId);
}