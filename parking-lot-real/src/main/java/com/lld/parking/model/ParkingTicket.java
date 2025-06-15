package com.lld.parking.model;

import com.lld.parking.enums.ParkingTicketStatus;
import com.lld.parking.enums.VehicleType;

import java.time.LocalDateTime;

public class ParkingTicket {
    private final String ticketId;
    private final String licensePlate;
    private final VehicleType vehicleType;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private final ParkingSpot parkingSpot;
    private ParkingTicketStatus status;
    private double amount;
    
    public ParkingTicket(String licensePlate, VehicleType vehicleType, ParkingSpot spot) {
        this.ticketId = generateTicketId();
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.parkingSpot = spot;
        this.entryTime = LocalDateTime.now();
        this.status = ParkingTicketStatus.ACTIVE;
    }
    
    private String generateTicketId() {
        return "TKT" + System.currentTimeMillis();
    }
    
    // Getters and Setters
    public String getTicketId() { return ticketId; }
    public String getLicensePlate() { return licensePlate; }
    public VehicleType getVehicleType() { return vehicleType; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    public ParkingSpot getParkingSpot() { return parkingSpot; }
    public ParkingTicketStatus getStatus() { return status; }
    public void setStatus(ParkingTicketStatus status) { this.status = status; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}