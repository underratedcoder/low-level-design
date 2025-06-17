package com.lld.parking.strategy;

import com.lld.parking.depth.enums.VehicleType;

public interface PricingStrategy {
    double calculateFare(VehicleType vehicleType, long hours);
}