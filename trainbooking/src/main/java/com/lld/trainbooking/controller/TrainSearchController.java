package com.lld.trainbooking.controller;// Controller

import java.util.*;

import com.lld.trainbooking.model.*;
import com.lld.trainbooking.service.TrainSearchService;
import lombok.AllArgsConstructor;

/**
 * Controller for handling train search requests
 */
@AllArgsConstructor
public class TrainSearchController {
    private final TrainSearchService trainSearchService;

    public TrainSearchController() {
        this.trainSearchService = new TrainSearchService();
    }

    public TrainSearchResponse searchTrains(TrainSearchRequest request) {
        try {
            // Validate request
            if (request == null ||
                    request.getSourceStationCode() == null ||
                    request.getDestinationStationCode() == null ||
                    request.getTravelDate() == null) {
                return TrainSearchResponse.builder()
                        .schedules(Collections.emptyList())
                        .message("Invalid request parameters")
                        .success(false)
                        .build();
            }

            // Search trains
            List<TrainSchedule> schedules = trainSearchService.searchTrains(
                    request.getSourceStationCode(),
                    request.getDestinationStationCode(),
                    request.getTravelDate()
            );

            if (schedules.isEmpty()) {
                return TrainSearchResponse.builder()
                        .schedules(Collections.emptyList())
                        .message("No trains found for the given route and date")
                        .success(false)
                        .build();
            }

            return TrainSearchResponse.builder()
                    .schedules(schedules)
                    .message("Trains found successfully")
                    .success(true)
                    .build();

        } catch (Exception e) {
            return TrainSearchResponse.builder()
                    .schedules(Collections.emptyList())
                    .message("Error occurred: " + e.getMessage())
                    .success(false)
                    .build();
        }
    }
}