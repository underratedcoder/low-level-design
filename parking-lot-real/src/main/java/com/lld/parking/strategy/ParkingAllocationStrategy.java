package com.lld.parking.strategy;

import com.lld.parking.depth.model.EntryPoint;
import com.lld.parking.depth.model.ParkingFloor;
import com.lld.parking.depth.model.ParkingSpot;
import com.lld.parking.depth.model.vehicle.Vehicle;

import java.util.List;

public interface ParkingAllocationStrategy {
    ParkingSpot findParkingSpot(Vehicle vehicle, List<ParkingFloor> floors, List<EntryPoint> entryPoints);
}