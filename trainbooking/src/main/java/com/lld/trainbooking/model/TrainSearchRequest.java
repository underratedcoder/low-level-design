package com.lld.trainbooking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Search request for trains
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainSearchRequest {
    private String sourceStationCode;
    private String destinationStationCode;
    private LocalDate travelDate;
}
