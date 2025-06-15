package com.lld.trainbooking.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import com.lld.trainbooking.dao.RouteSegmentRepository;
import com.lld.trainbooking.dao.SeatRepository;
import com.lld.trainbooking.dao.TravelClassRepository;
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

/**
 * Service for calculating fares
 */
@AllArgsConstructor
class FareCalculationService {
    private final TravelClassRepository travelClassRepository;

    public FareCalculationService() {
        this.travelClassRepository = new TravelClassRepository();
    }

    public Map<ClassType, BigDecimal> calculateFares(Long trainId, String sourceStationCode,
                                                     String destinationStationCode) {
        RouteSegmentRepository routeSegmentRepo = new RouteSegmentRepository();

        // Find the route segments for source and destination
        RouteSegment sourceSegment = routeSegmentRepo.findByTrainAndStation(trainId, sourceStationCode);
        RouteSegment destSegment = routeSegmentRepo.findByTrainAndStation(trainId, destinationStationCode);

        if (sourceSegment == null || destSegment == null) {
            return Collections.emptyMap();
        }

        // Calculate distance between stations
        int distanceCovered = destSegment.getDistanceFromOrigin() - sourceSegment.getDistanceFromOrigin();

        // Get base fares for each class
        List<TravelClass> classes = travelClassRepository.findByTrainId(trainId);
        Map<ClassType, BigDecimal> fares = new HashMap<>();

        for (TravelClass travelClass : classes) {
            // Apply distance-based fare calculation
            // For simplicity: fare = base fare + (distance factor * distance)
            BigDecimal distanceFactor = new BigDecimal("0.1");  // 0.1 per km
            BigDecimal additionalFare = distanceFactor.multiply(new BigDecimal(distanceCovered));
            BigDecimal totalFare = travelClass.getBaseFare().add(additionalFare);

            // Round to nearest 10
            totalFare = totalFare.setScale(0, RoundingMode.CEILING);
            int fareInt = totalFare.intValue();
            fareInt = (fareInt / 10) * 10;  // Round to nearest 10
            totalFare = new BigDecimal(fareInt);

            fares.put(travelClass.getClassType(), totalFare);
        }

        return fares;
    }
}