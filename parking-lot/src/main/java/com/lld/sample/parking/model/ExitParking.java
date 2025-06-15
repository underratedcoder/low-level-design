package com.lld.sample.parking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ExitParking {
    private String bookingId;
    private double fare;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
}
