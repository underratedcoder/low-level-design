package com.lld.trainbooking.model;

import com.lld.trainbooking.enums.ClassType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Travel class for a specific train
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelClass {
    private Long id;
    private Train train;
    private ClassType classType;
    private BigDecimal baseFare;
    private int totalSeats;
}