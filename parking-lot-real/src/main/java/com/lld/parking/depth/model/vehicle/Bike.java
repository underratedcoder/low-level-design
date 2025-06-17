package com.lld.parking.depth.model.vehicle;

import com.lld.parking.depth.enums.VehicleType;

public class Bike extends Vehicle {
    public Bike(String licensePlate) {
        super(licensePlate, VehicleType.BIKE);
    }
}