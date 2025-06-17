package com.lld.parking.quick.model;

import com.lld.parking.quick.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    private String number;
    private VehicleType type;
}