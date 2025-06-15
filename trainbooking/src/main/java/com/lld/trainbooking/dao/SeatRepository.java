package com.lld.trainbooking.dao;

import com.lld.trainbooking.enums.ClassType;
import com.lld.trainbooking.model.RouteSegment;
import com.lld.trainbooking.model.Station;
import com.lld.trainbooking.model.Train;
import com.lld.trainbooking.model.TravelClass;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository to handle booking and seat allocations
 * (Simplified for the train search implementation)
 */
@AllArgsConstructor
public class SeatRepository {
    // In a real implementation, this would interact with a database
    // For now, we'll simulate seat availability

    public Map<ClassType, Integer> getAvailableSeats(Long trainId, String sourceStationCode,
                                                     String destinationStationCode, LocalDate travelDate) {
        // Get travel classes for the train
        TravelClassRepository travelClassRepo = new TravelClassRepository();
        List<TravelClass> classes = travelClassRepo.findByTrainId(trainId);

        // Simulate available seats (in a real system, this would check actual bookings)
        Map<ClassType, Integer> availableSeats = new HashMap<>();
        for (TravelClass travelClass : classes) {
            // For simplicity, assume 80% of seats are available
            int available = (int) (travelClass.getTotalSeats() * 0.8);
            availableSeats.put(travelClass.getClassType(), available);
        }

        return availableSeats;
    }
}