package com.lld.airline.model;

import com.lld.airline.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Booking {
    private Long bookingId;
    private Long userId;
    private Long flightId;
    private int noOfTravelers;
    private List<Long> seatIds;
    private double amount;
    private BookingStatus status;
    private List<Passenger> passengers;
    private LocalDateTime createdDatetime;
}