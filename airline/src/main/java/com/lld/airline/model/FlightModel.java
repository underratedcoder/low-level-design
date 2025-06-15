package com.lld.airline.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlightModel {
    private Long flightId;
    private String source;
    private String destination;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private Long airplaneId;
    private int totalSeats;
    private int availableSeats;

    public void setAvailableSeats(int bookedSeat) {
        synchronized (this) {
            this.availableSeats = this.availableSeats - bookedSeat;
        }
    }
}