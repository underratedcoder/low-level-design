package com.lld.sample.parking.service;

import com.lld.sample.parking.enums.VehicleType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class FareCalculator {
    private static final double BASE_RATE = 10.0; // Base rate per hour
    private static final double MORNING_MULTIPLIER = 1.0;
    private static final double AFTERNOON_MULTIPLIER = 1.5;
    private static final double EVENING_MULTIPLIER = 2.0;

    public double calculateFare(LocalDateTime entryTime, LocalDateTime exitTime, VehicleType vehicleType) {
        double totalFare = 0.0;
        LocalDateTime currentTime = entryTime;

        while (currentTime.isBefore(exitTime)) {
            LocalDateTime nextSlotTime = getNextSlotTime(currentTime);
            if (nextSlotTime.isAfter(exitTime)) {
                nextSlotTime = exitTime;
            }

            long hours = ChronoUnit.HOURS.between(currentTime, nextSlotTime);
            double multiplier = getTimeMultiplier(currentTime);
            totalFare += BASE_RATE * hours * multiplier * getVehicleMultiplier(vehicleType);

            currentTime = nextSlotTime;
        }

        return totalFare;
    }

    private LocalDateTime getNextSlotTime(LocalDateTime currentTime) {
        int hour = currentTime.getHour();
        if (hour < 6) {
            return currentTime.withHour(6).withMinute(0).withSecond(0);
        } else if (hour < 12) {
            return currentTime.withHour(12).withMinute(0).withSecond(0);
        } else if (hour < 18) {
            return currentTime.withHour(18).withMinute(0).withSecond(0);
        } else {
            return currentTime.plusDays(1).withHour(6).withMinute(0).withSecond(0);
        }
    }

    private double getTimeMultiplier(LocalDateTime time) {
        int hour = time.getHour();
        if (hour >= 6 && hour < 12) {
            return MORNING_MULTIPLIER;
        } else if (hour >= 12 && hour < 18) {
            return AFTERNOON_MULTIPLIER;
        } else {
            return EVENING_MULTIPLIER;
        }
    }

    private double getVehicleMultiplier(VehicleType vehicleType) {
        switch (vehicleType) {
            case CAR:
                return 1.0;
            case BIKE:
                return 0.5;
            case TRUCK:
                return 2.0;
            default:
                throw new IllegalArgumentException("Invalid vehicle type");
        }
    }
}