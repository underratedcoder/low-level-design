package com.lld.parking.depth.model;

import com.lld.parking.depth.enums.PaymentMethod;
import com.lld.parking.depth.service.ParkingLot;

public class ExitPoint {
    private String exitId;
    private ParkingLot parkingLot;
    
    public ExitPoint(String exitId, ParkingLot parkingLot) {
        this.exitId = exitId;
        this.parkingLot = parkingLot;
    }
    
    public boolean processExit(ParkingTicket ticket, PaymentMethod paymentMethod) {
        return parkingLot.unparkVehicle(ticket, paymentMethod);
    }
    
    public String getExitId() { return exitId; }
}