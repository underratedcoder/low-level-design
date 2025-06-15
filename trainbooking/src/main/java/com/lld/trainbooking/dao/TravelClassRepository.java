package com.lld.trainbooking.dao;

import com.lld.trainbooking.enums.ClassType;
import com.lld.trainbooking.model.RouteSegment;
import com.lld.trainbooking.model.Station;
import com.lld.trainbooking.model.Train;
import com.lld.trainbooking.model.TravelClass;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Repository for TravelClass-related database operations
 */
@AllArgsConstructor
public class TravelClassRepository {
    // Simulating database with in-memory storage
    private static final List<TravelClass> travelClasses = new ArrayList<>();
    private static final TrainRepository trainRepository = new TrainRepository();

    static {
        // Initialize with some sample data
        Train train1 = trainRepository.findById(1L);
        Train train2 = trainRepository.findById(2L);
        Train train3 = trainRepository.findById(3L);

        // Travel classes for Train 1
        travelClasses.add(TravelClass.builder()
                .id(1L)
                .train(train1)
                .classType(ClassType.SLEEPER)
                .baseFare(new BigDecimal("500.00"))
                .totalSeats(100)
                .build());

        travelClasses.add(TravelClass.builder()
                .id(2L)
                .train(train1)
                .classType(ClassType.AC_3_TIER)
                .baseFare(new BigDecimal("800.00"))
                .totalSeats(80)
                .build());

        travelClasses.add(TravelClass.builder()
                .id(3L)
                .train(train1)
                .classType(ClassType.AC_2_TIER)
                .baseFare(new BigDecimal("1200.00"))
                .totalSeats(60)
                .build());

        // Travel classes for Train 2
        travelClasses.add(TravelClass.builder()
                .id(4L)
                .train(train2)
                .classType(ClassType.SLEEPER)
                .baseFare(new BigDecimal("550.00"))
                .totalSeats(120)
                .build());

        travelClasses.add(TravelClass.builder()
                .id(5L)
                .train(train2)
                .classType(ClassType.AC_3_TIER)
                .baseFare(new BigDecimal("850.00"))
                .totalSeats(90)
                .build());

        travelClasses.add(TravelClass.builder()
                .id(6L)
                .train(train2)
                .classType(ClassType.AC_2_TIER)
                .baseFare(new BigDecimal("1250.00"))
                .totalSeats(70)
                .build());

        // Travel classes for Train 3
        travelClasses.add(TravelClass.builder()
                .id(7L)
                .train(train3)
                .classType(ClassType.SLEEPER)
                .baseFare(new BigDecimal("450.00"))
                .totalSeats(110)
                .build());

        travelClasses.add(TravelClass.builder()
                .id(8L)
                .train(train3)
                .classType(ClassType.AC_3_TIER)
                .baseFare(new BigDecimal("750.00"))
                .totalSeats(85)
                .build());
    }

    public List<TravelClass> findByTrainId(Long trainId) {
        return travelClasses.stream()
                .filter(travelClass -> travelClass.getTrain().getId().equals(trainId))
                .collect(Collectors.toList());
    }
}