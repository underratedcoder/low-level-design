package com.lld.sample.parking.model;

public class Parking {
    private String parkingId;
    private String address;

    // Constructor, Getters, and Setters
    public Parking(String parkingId, String address) {
        this.parkingId = parkingId;
        this.address = address;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}