package com.lld.parking.strategy;

import com.lld.parking.depth.model.EntryPoint;
import com.lld.parking.depth.model.ParkingFloor;
import com.lld.parking.depth.model.ParkingSpot;
import com.lld.parking.depth.model.vehicle.Vehicle;

import java.util.List;

public class GroundFloorPriorityStrategy implements ParkingAllocationStrategy {
    @Override
    public ParkingSpot findParkingSpot(Vehicle vehicle, List<ParkingFloor> floors, List<EntryPoint> entryPoints) {
        System.out.println("Using Ground Floor Priority allocation strategy");
        
        // First try to find spot on ground floor (floor 1)
        for (ParkingFloor floor : floors) {
            if (floor.getFloorNumber() == 1) {
                ParkingSpot availableSpot = floor.findAvailableSpot(vehicle);
                if (availableSpot != null) {
                    System.out.println("Allocated spot: " + availableSpot.getSpotId() + " (ground floor)");
                    return availableSpot;
                }
            }
        }
        
        // If ground floor is full, find any available spot on other floors
        System.out.println("Ground floor full, finding alternative spot");
        for (ParkingFloor floor : floors) {
            if (floor.getFloorNumber() != 1) {
                ParkingSpot availableSpot = floor.findAvailableSpot(vehicle);
                if (availableSpot != null) {
                    System.out.println("Allocated spot: " + availableSpot.getSpotId() + " (floor " + floor.getFloorNumber() + ")");
                    return availableSpot;
                }
            }
        }
        
        return null;
    }
}