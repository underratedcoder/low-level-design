package com.lld.parking.strategy;

import com.lld.parking.depth.model.EntryPoint;
import com.lld.parking.depth.model.ParkingFloor;
import com.lld.parking.depth.model.ParkingSpot;
import com.lld.parking.depth.model.vehicle.Vehicle;

import java.util.List;

public class NearestToEntryStrategy implements ParkingAllocationStrategy {
    @Override
    public ParkingSpot findParkingSpot(Vehicle vehicle, List<ParkingFloor> floors, List<EntryPoint> entryPoints) {
        System.out.println("Using Nearest to Entry Gate allocation strategy");
        
        // For simplicity, we'll consider the first entry point as reference
        // In real implementation, this would calculate actual distances
        
        // Priority: Lower floor numbers are considered closer to entry
        // Within same floor, spots with lower IDs are considered closer
        
        ParkingSpot bestSpot = null;
        int bestFloor = Integer.MAX_VALUE;
        String bestSpotId = null;
        
        for (ParkingFloor floor : floors) {
            ParkingSpot availableSpot = floor.findAvailableSpot(vehicle);
            if (availableSpot != null) {
                // Check if this spot is "nearer" to entry
                if (floor.getFloorNumber() < bestFloor || 
                    (floor.getFloorNumber() == bestFloor && 
                     (bestSpotId == null || availableSpot.getSpotId().compareTo(bestSpotId) < 0))) {
                    bestSpot = availableSpot;
                    bestFloor = floor.getFloorNumber();
                    bestSpotId = availableSpot.getSpotId();
                }
            }
        }
        
        if (bestSpot != null) {
            System.out.println("Allocated spot: " + bestSpot.getSpotId() + " (closest to entry)");
        }
        
        return bestSpot;
    }
}
