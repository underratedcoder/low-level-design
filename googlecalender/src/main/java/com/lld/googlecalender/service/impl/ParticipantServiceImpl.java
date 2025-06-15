package com.lld.googlecalender.service.impl;

import com.lld.googlecalender.enums.ParticipantResponseStatus;
import com.lld.googlecalender.model.Event;
import com.lld.googlecalender.model.Participant;
import com.lld.googlecalender.repository.IParticipantRepository;
import com.lld.googlecalender.service.ICalenderService;
//import com.lld.googlecalender.service.NotificationService;
import com.lld.googlecalender.service.IParticipantService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class ParticipantServiceImpl implements IParticipantService {
    private final IParticipantRepository IParticipantRepository;
    private final ICalenderService ICalenderService;
//    private final NotificationService notificationService;

    @Override
    public Participant addParticipant(UUID eventId, UUID userId) {
        // Check if the participant already exists
        Optional<Participant> existingParticipant = IParticipantRepository.findByUserIdAndEventId(userId, eventId);
        
        if (existingParticipant.isPresent()) {
            return existingParticipant.get();
        }
        
        // Create a new participant
        Participant participant = Participant.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .eventId(eventId)
                .responseStatus(ParticipantResponseStatus.PENDING)
                .build();
        
        return IParticipantRepository.save(participant);
    }

    @Override
    public Participant updateParticipantResponse(UUID eventId, UUID userId, 
                                               ParticipantResponseStatus status) {
        // Find the participant
        Participant participant = IParticipantRepository.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new RuntimeException("Participant not found"));
        
        // Update the response
        participant.setResponseStatus(status);
        Participant updatedParticipant = IParticipantRepository.save(participant);
        
        // Notify about the response update
       // notificationService.notifyParticipantResponse(eventId, userId, status);
        
        return updatedParticipant;
    }

    @Override
    public List<Participant> updateParticipantResponseForSeries(UUID seriesId, UUID userId, 
                                                             ParticipantResponseStatus status) {
        // Get all events in the series
        List<Event> events = ICalenderService.getEventsBySeriesId(seriesId);
        List<Participant> updatedParticipants = new ArrayList<>();
        
        // Update response for each event
        for (Event event : events) {
            try {
                Participant participant = updateParticipantResponse(event.getId(), userId, status);
                updatedParticipants.add(participant);
            } catch (RuntimeException e) {
                // If participant doesn't exist for this event, add them
                Participant newParticipant = addParticipant(event.getId(), userId);
                newParticipant.setResponseStatus(status);
                updatedParticipants.add(IParticipantRepository.save(newParticipant));
            }
        }
        
        // Notify about the series response update
        //notificationService.notifyParticipantSeriesResponse(seriesId, userId, status);
        
        return updatedParticipants;
    }

    @Override
    public List<Participant> getParticipantsByEventId(UUID eventId) {
        return IParticipantRepository.findByEventId(eventId);
    }

    @Override
    public List<Participant> getParticipationsByUserId(UUID userId) {
        return IParticipantRepository.findByUserId(userId);
    }

    @Override
    public void removeParticipant(UUID eventId, UUID userId) {
        // Find the participant
        Participant participant = IParticipantRepository.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new RuntimeException("Participant not found"));
        
        // Delete the participant
        IParticipantRepository.delete(participant.getId());
        
        // Notify about the removal
        //notificationService.notifyParticipantRemoval(eventId, userId);
    }

    @Override
    public void removeParticipantFromSeries(UUID seriesId, UUID userId) {
        // Get all events in the series
        List<Event> events = ICalenderService.getEventsBySeriesId(seriesId);
        
        // Remove participant from each event
        for (Event event : events) {
            try {
                removeParticipant(event.getId(), userId);
            } catch (RuntimeException e) {
                // Ignore if participant doesn't exist for this event
            }
        }
        
        // Notify about the series removal
        //notificationService.notifyParticipantSeriesRemoval(seriesId, userId);
    }

    public boolean isUserParticipant(UUID eventId, UUID userId) {
        return IParticipantRepository.findByUserIdAndEventId(userId, eventId).isPresent();
    }
}