package com.lld.trainbooking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a train station
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Station {
    private Long id;
    private String code;
    private String name;
    private String city;
}