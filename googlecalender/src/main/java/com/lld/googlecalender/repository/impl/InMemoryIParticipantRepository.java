package com.lld.googlecalender.repository.impl;

import com.lld.googlecalender.model.Participant;
import com.lld.googlecalender.repository.IParticipantRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryIParticipantRepository implements IParticipantRepository {
    private final Map<UUID, Participant> participants = new ConcurrentHashMap<>();

    @Override
    public Participant save(Participant participant) {
        if (participant.getId() == null) {
            participant.setId(UUID.randomUUID());
        }
        participants.put(participant.getId(), participant);
        return participant;
    }

    @Override
    public Participant findById(UUID id) {
        return participants.get(id);
    }

    @Override
    public List<Participant> findByEventId(UUID eventId) {
        return participants.values().stream()
                .filter(participant -> eventId.equals(participant.getEventId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Participant> findByUserId(UUID userId) {
        return participants.values().stream()
                .filter(participant -> userId.equals(participant.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        participants.remove(id);
    }

//    @Override
//    public Optional<Participant> findByUserIdAndEventId(UUID userId, UUID eventId) {
//        return participants.values().stream()
//                .filter(participant ->
//                    userId.equals(participant.getUserId()) &&
//                    eventId.equals(participant.getEventId()))
//                .findFirst();
//    }

}