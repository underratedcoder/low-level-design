package com.lld.sample.parking.model;

import com.lld.sample.parking.enums.VehicleType;

public class Spot {
    private String spotId;
    private String parkingId;
    private int floorNo;
    private VehicleType vehicleType; // CAR, BIKE, TRUCK

    // Constructor, Getters, and Setters
    public Spot(String spotId, String parkingId, int floorNo, VehicleType vehicleType) {
        this.spotId = spotId;
        this.parkingId = parkingId;
        this.floorNo = floorNo;
        this.vehicleType = vehicleType;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public int getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(int floorNo) {
        this.floorNo = floorNo;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}