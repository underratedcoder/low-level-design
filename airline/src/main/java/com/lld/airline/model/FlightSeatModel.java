package com.lld.airline.model;

import com.lld.airline.enums.SeatSize;
import com.lld.airline.enums.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightSeatModel {
    private Long flightId;
    private Long seatId;
    private SeatStatus seatStatus;  // FREE, RESERVED, BOOKED, MAINTENANCE
    private SeatSize seatSize;      // N (Normal), L (Large), XL (Extra Large)
    private int seatPrice;
    private String bookedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    //private Long processId;
}
