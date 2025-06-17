package com.lld.parking.quick.model;

import com.lld.parking.quick.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpot {
    private int spotId;
    private VehicleType type;
    private boolean isOccupied;
    private Vehicle parkedVehicle;
}