package com.lld.parking.strategy;

import com.lld.parking.depth.enums.VehicleType;

import java.util.HashMap;
import java.util.Map;

public class HourlyPricingStrategy implements PricingStrategy {
    private Map<VehicleType, Double> hourlyRates;
    
    public HourlyPricingStrategy() {
        hourlyRates = new HashMap<>();
        hourlyRates.put(VehicleType.BIKE, 10.0);
        hourlyRates.put(VehicleType.CAR, 20.0);
    }
    
    @Override
    public double calculateFare(VehicleType vehicleType, long hours) {
        if (hours == 0) hours = 1; // Minimum 1 hour charge
        return hourlyRates.get(vehicleType) * hours;
    }
}