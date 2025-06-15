package com.lld.trainbooking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a train
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Train {
    private Long id;
    private String trainNumber;
    private String name;
    private Station sourceStation;
    private Station destinationStation;
    private boolean active;
}