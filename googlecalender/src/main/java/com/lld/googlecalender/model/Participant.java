package com.lld.googlecalender.model;

import com.lld.googlecalender.enums.ParticipantResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participant {
    private UUID id;
    private UUID userId;
    private UUID eventId;
    @Builder.Default
    private ParticipantResponseStatus responseStatus = ParticipantResponseStatus.PENDING;
}