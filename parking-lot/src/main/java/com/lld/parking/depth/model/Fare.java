package com.lld.parking.depth.model;

public class Fare {
    private String fareId;
    private String bookingId;
    private double fareAmount;

    // Constructor, Getters, and Setters
    public Fare(String fareId, String bookingId, double fareAmount) {
        this.fareId = fareId;
        this.bookingId = bookingId;
        this.fareAmount = fareAmount;
    }

    public String getFareId() {
        return fareId;
    }

    public void setFareId(String fareId) {
        this.fareId = fareId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public double getFareAmount() {
        return fareAmount;
    }

    public void setFareAmount(double fareAmount) {
        this.fareAmount = fareAmount;
    }
}