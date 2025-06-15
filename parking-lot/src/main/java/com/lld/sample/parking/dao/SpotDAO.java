package com.lld.sample.parking.dao;

import com.lld.sample.parking.model.Spot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpotDAO {
    private List<Spot> spots = new ArrayList<>();

    public List<Spot> getParkingSpots(String parkingId, Integer floorNo) {
        // Simulate fetching spots from the database
        return spots.stream()
                .filter(spot -> spot.getParkingId().equals(parkingId))
                .filter(spot -> floorNo == null || spot.getFloorNo() == floorNo)
                .collect(Collectors.toList());
    }
}