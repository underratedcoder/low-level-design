package com.lld.googlecalender.repository;

import com.lld.googlecalender.model.Participant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IParticipantRepository {
    Participant save(Participant participant);
    Participant findById(UUID id);
    Optional<Participant> findByUserIdAndEventId(UUID userId, UUID eventId);
    List<Participant> findByEventId(UUID eventId);
    List<Participant> findByUserId(UUID userId);
    void delete(UUID id);
}