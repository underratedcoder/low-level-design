package com.lld.parking.depth.model;

import com.lld.parking.depth.enums.ParkingSpotType;
import com.lld.parking.depth.model.vehicle.Vehicle;

public class ParkingSpot {
    private String spotId;
    private ParkingSpotType type;
    private boolean isFree;
    private Vehicle vehicle;
    private int floor;
    
    public ParkingSpot(String spotId, ParkingSpotType type, int floor) {
        this.spotId = spotId;
        this.type = type;
        this.floor = floor;
        this.isFree = true;
    }
    
    public boolean canFitVehicle(Vehicle vehicle) {
        switch (vehicle.getType()) {
            case BIKE:
                return type == ParkingSpotType.SMALL ||
                       type == ParkingSpotType.MEDIUM;
            case CAR:
                return type == ParkingSpotType.MEDIUM;
            default:
                return false;
        }
    }
    
    public boolean assignVehicle(Vehicle vehicle) {
        if (isFree && canFitVehicle(vehicle)) {
            this.vehicle = vehicle;
            this.isFree = false;
            return true;
        }
        return false;
    }
    
    public void removeVehicle() {
        this.vehicle = null;
        this.isFree = true;
    }
    
    // Getters
    public String getSpotId() { return spotId; }

    public ParkingSpotType getType() { return type; }

    public boolean isFree() { return isFree; }

    public Vehicle getVehicle() { return vehicle; }

    public int getFloor() { return floor; }
}