package com.lld.trainbooking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Search response containing train schedules
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainSearchResponse {
    private List<TrainSchedule> schedules;
    private String message;
    private boolean success;
}