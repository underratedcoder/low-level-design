package com.lld.parking.strategy;

import com.lld.parking.model.EntryPoint;
import com.lld.parking.model.ParkingFloor;
import com.lld.parking.model.ParkingSpot;
import com.lld.parking.model.vehicle.Vehicle;

import java.util.List;

public interface ParkingAllocationStrategy {
    ParkingSpot findParkingSpot(Vehicle vehicle, List<ParkingFloor> floors, List<EntryPoint> entryPoints);
}