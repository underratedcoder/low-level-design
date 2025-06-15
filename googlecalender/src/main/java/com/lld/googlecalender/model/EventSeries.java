package com.lld.googlecalender.model;

import com.lld.googlecalender.enums.RecurrenceType;
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
public class EventSeries {
    private UUID id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UUID createdBy;
    private RecurrenceType recurrenceType;
//    private int recurrenceInterval;
    private LocalDateTime seriesEndDate;
}