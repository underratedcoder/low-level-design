package com.lld.airline.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class SeatReservation {
    private Long flightId;
    private Long userId;
    private int noOfSeats;
    private List<Long> seatIds;
}
