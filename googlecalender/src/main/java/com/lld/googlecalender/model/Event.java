package com.lld.googlecalender.model;

import com.lld.googlecalender.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private UUID seriesId; // ID of the parent event for recurring events
    private UUID id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Builder.Default
    private EventStatus status = EventStatus.SCHEDULED;
}