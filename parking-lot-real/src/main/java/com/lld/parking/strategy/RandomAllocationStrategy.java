package com.lld.parking.strategy;

import com.lld.parking.model.EntryPoint;
import com.lld.parking.model.ParkingFloor;
import com.lld.parking.model.ParkingSpot;
import com.lld.parking.model.vehicle.Vehicle;

import java.util.List;

public class RandomAllocationStrategy implements ParkingAllocationStrategy {
    @Override
    public ParkingSpot findParkingSpot(Vehicle vehicle, List<ParkingFloor> floors, List<EntryPoint> entryPoints) {
        System.out.println("Using Random allocation strategy");
        
        // Simple first-available allocation
        for (ParkingFloor floor : floors) {
            ParkingSpot availableSpot = floor.findAvailableSpot(vehicle);
            if (availableSpot != null) {
                System.out.println("Allocated spot: " + availableSpot.getSpotId() + " (random)");
                return availableSpot;
            }
        }
        
        return null;
    }
}