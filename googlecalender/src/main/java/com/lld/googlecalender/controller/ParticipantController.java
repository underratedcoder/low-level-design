package com.lld.googlecalender.controller;

import com.lld.googlecalender.enums.ParticipantResponseStatus;
import com.lld.googlecalender.model.Participant;
import com.lld.googlecalender.service.IParticipantService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ParticipantController {
    private final IParticipantService IParticipantService;
    
    public Participant respondToEvent(UUID eventId, UUID userId, ParticipantResponseStatus status) {
        return IParticipantService.updateParticipantResponse(eventId, userId, status);
    }
    
    public List<Participant> respondToEventSeries(UUID seriesId, UUID userId, ParticipantResponseStatus status) {
        return IParticipantService.updateParticipantResponseForSeries(seriesId, userId, status);
    }
    
    public List<Participant> getEventParticipants(UUID eventId) {
        return IParticipantService.getParticipantsByEventId(eventId);
    }
    
    public List<Participant> getUserParticipations(UUID userId) {
        return IParticipantService.getParticipationsByUserId(userId);
    }
    
    public void removeParticipant(UUID eventId, UUID userId) {
        IParticipantService.removeParticipant(eventId, userId);
    }
    
    public void removeParticipantFromSeries(UUID seriesId, UUID userId) {
        IParticipantService.removeParticipantFromSeries(seriesId, userId);
    }
}