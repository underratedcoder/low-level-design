package com.lld.trainbooking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * Represents a segment of a train's route
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteSegment {
    private Long id;
    private Train train;
    private Station station;
    private int sequenceNumber;
    private LocalTime arrivalTime;
    private LocalTime departureTime;
    private int distanceFromOrigin;
}