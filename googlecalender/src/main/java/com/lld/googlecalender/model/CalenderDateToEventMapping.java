package com.lld.googlecalender.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalenderDateToEventMapping {
    private UUID calendarId;
    private LocalDate eventDate;
    private UUID eventId;
}
