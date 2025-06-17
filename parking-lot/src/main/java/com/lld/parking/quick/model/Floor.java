package com.lld.parking.quick.model;

import com.lld.parking.quick.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Floor {
    private int floorNumber;
    private Map<VehicleType, List<ParkingSpot>> spotsByType;
}