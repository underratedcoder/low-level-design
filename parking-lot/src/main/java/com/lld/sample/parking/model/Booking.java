package com.lld.sample.parking.model;

import com.lld.sample.parking.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Booking {
    private String bookingId;
    private String vehicleNo;
    private Long userId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private String parkingId;
    private String spotId;
    private VehicleType vehicleType;
}