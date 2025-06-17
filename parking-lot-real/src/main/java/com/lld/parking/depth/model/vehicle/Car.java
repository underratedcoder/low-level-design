package com.lld.parking.depth.model.vehicle;

import com.lld.parking.depth.enums.VehicleType;

public class Car extends Vehicle {
    public Car(String licensePlate) {
        super(licensePlate, VehicleType.CAR);
    }
}