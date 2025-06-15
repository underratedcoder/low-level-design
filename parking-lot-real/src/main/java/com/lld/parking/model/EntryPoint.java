package com.lld.parking.model;

import com.lld.parking.model.vehicle.Vehicle;
import com.lld.parking.service.ParkingLot;

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