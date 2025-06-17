package com.lld.rating.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmployeeRatingEntry {
    private final String employeeId;
    private final double averageRating;
}
