package com.lld.trainbooking.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import com.lld.trainbooking.dao.RouteSegmentRepository;
import com.lld.trainbooking.dao.SeatRepository;
import com.lld.trainbooking.enums.ClassType;
import com.lld.trainbooking.model.*;
import com.lld.trainbooking.service.FareCalculationService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

// Services


/**
 * Service for searching trains
 */
@AllArgsConstructor
public
class TrainSearchService {
    private final RouteSegmentRepository routeSegmentRepository;
    private final SeatRepository seatRepository;
    private final FareCalculationService fareCalculationService;

    public TrainSearchService() {
        this.routeSegmentRepository = new RouteSegmentRepository();
        this.seatRepository = new SeatRepository();
        this.fareCalculationService = new FareCalculationService();
    }

    public List<TrainSchedule> searchTrains(String sourceStationCode, String destinationStationCode, LocalDate travelDate) {
        // Validate input
        if (sourceStationCode == null || destinationStationCode == null || travelDate == null) {
            return Collections.emptyList();
        }

        // Find trains that connect the two stations
        List<Train> trains = routeSegmentRepository.findTrainsConnectingStations(sourceStationCode, destinationStationCode);

        List<TrainSchedule> schedules = new ArrayList<>();

        for (Train train : trains) {
            // Get route segments for source and destination
            RouteSegment sourceSegment = routeSegmentRepository.findByTrainAndStation(train.getId(), sourceStationCode);
            RouteSegment destSegment = routeSegmentRepository.findByTrainAndStation(train.getId(), destinationStationCode);

            if (sourceSegment == null || destSegment == null) {
                continue;  // Skip if segments not found
            }

            // Calculate distance covered
            int distanceCovered = destSegment.getDistanceFromOrigin() - sourceSegment.getDistanceFromOrigin();

            // Create train schedule
            TrainSchedule schedule = TrainSchedule.builder()
                    .train(train)
                    .travelDate(travelDate)
                    .sourceStation(sourceSegment.getStation())
                    .destinationStation(destSegment.getStation())
                    .departureTime(sourceSegment.getDepartureTime())
                    .arrivalTime(destSegment.getArrivalTime())
                    .distanceCovered(distanceCovered)
                    .build();

            // Get available seats
            Map<ClassType, Integer> availableSeats = seatRepository.getAvailableSeats(
                    train.getId(), sourceStationCode, destinationStationCode, travelDate);

            // Calculate fares
            Map<ClassType, BigDecimal> fares = fareCalculationService.calculateFares(
                    train.getId(), sourceStationCode, destinationStationCode);

            // Add class availability and fares to schedule
            for (ClassType classType : availableSeats.keySet()) {
                schedule.addClassAvailability(classType, availableSeats.get(classType), fares.get(classType));
            }

            schedules.add(schedule);
        }

        return schedules;
    }
}
