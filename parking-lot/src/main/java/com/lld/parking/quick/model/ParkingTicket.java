package com.lld.parking.quick.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingTicket {
    private String ticketId;
    private Vehicle vehicle;
    private LocalDateTime entryTime;
    private int floorNumber;
    private int spotId;
}