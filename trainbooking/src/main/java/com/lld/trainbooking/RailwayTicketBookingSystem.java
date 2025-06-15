package com.lld.trainbooking;

import com.lld.trainbooking.controller.TrainSearchController;
import com.lld.trainbooking.enums.ClassType;
import com.lld.trainbooking.model.TrainSchedule;
import com.lld.trainbooking.model.TrainSearchRequest;
import com.lld.trainbooking.model.TrainSearchResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

// Main application to demonstrate the search functionality
public class RailwayTicketBookingSystem {
    public static void main(String[] args) {
        // Create controller
        TrainSearchController controller = new TrainSearchController();

        // Create search request for train from station D to M on 2025-04-25
        TrainSearchRequest request = TrainSearchRequest.builder()
                .sourceStationCode("D")
                .destinationStationCode("M")
                .travelDate(LocalDate.of(2025, 4, 25))
                .build();

        // Perform search
        TrainSearchResponse response = controller.searchTrains(request);

        // Display results
        System.out.println("Search success: " + response.isSuccess());
        System.out.println("Message: " + response.getMessage());

        if (response.isSuccess()) {
            System.out.println("\nFound " + response.getSchedules().size() + " trains:");

            for (TrainSchedule schedule : response.getSchedules()) {
                System.out.println("\nTrain: " + schedule.getTrain().getTrainNumber() + " - " + schedule.getTrain().getName());
                System.out.println("From: " + schedule.getSourceStation().getName() + " (" + schedule.getSourceStation().getCode() + ")");
                System.out.println("To: " + schedule.getDestinationStation().getName() + " (" + schedule.getDestinationStation().getCode() + ")");
                System.out.println("Departure: " + schedule.getDepartureTime());
                System.out.println("Arrival: " + schedule.getArrivalTime());
                System.out.println("Distance: " + schedule.getDistanceCovered() + " km");

                System.out.println("\nAvailable Classes:");
                for (Map.Entry<ClassType, Integer> entry : schedule.getAvailableSeats().entrySet()) {
                    ClassType classType = entry.getKey();
                    int seats = entry.getValue();
                    BigDecimal fare = schedule.getFares().get(classType);

                    System.out.println("  " + classType + " (" + classType.getCode() + "): " +
                            seats + " seats available, Fare: Rs. " + fare);
                }
            }
        }
    }
}