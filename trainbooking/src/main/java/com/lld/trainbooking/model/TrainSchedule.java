package com.lld.trainbooking.model;

import com.lld.trainbooking.enums.ClassType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Train schedule with availability information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainSchedule {
    private Train train;
    private LocalDate travelDate;
    private Station sourceStation;
    private Station destinationStation;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int distanceCovered;
    @Builder.Default
    private Map<ClassType, Integer> availableSeats = new HashMap<>();
    @Builder.Default
    private Map<ClassType, BigDecimal> fares = new HashMap<>();

    public void addClassAvailability(ClassType classType, int availableSeats, BigDecimal fare) {
        this.availableSeats.put(classType, availableSeats);
        this.fares.put(classType, fare);
    }
}