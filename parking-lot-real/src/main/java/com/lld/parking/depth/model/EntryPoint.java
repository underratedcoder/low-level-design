package com.lld.parking.depth.model;

import com.lld.parking.depth.model.vehicle.Vehicle;
import com.lld.parking.depth.service.ParkingLot;

public class EntryPoint {
    private String entryId;
    private ParkingLot parkingLot;
    
    public EntryPoint(String entryId, ParkingLot parkingLot) {
        this.entryId = entryId;
        this.parkingLot = parkingLot;
    }
    
    public ParkingTicket issueTicket(Vehicle vehicle) {
        return parkingLot.parkVehicle(vehicle);
    }
    
    public String getEntryId() { return entryId; }
}