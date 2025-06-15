package com.lld.parking.model;

import com.lld.parking.enums.ParkingSpotType;
import com.lld.parking.enums.VehicleType;
import com.lld.parking.model.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Parking Floor class
public class ParkingFloor {
    private final int floorNumber;
    private final Map<ParkingSpotType, List<ParkingSpot>> parkingSpots;
    private final Map<ParkingSpotType, Integer> availableSpots;

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.parkingSpots = new HashMap<>();
        this.availableSpots = new HashMap<>();

        // Initialize maps
        for (ParkingSpotType type : ParkingSpotType.values()) {
            parkingSpots.put(type, new ArrayList<>());
            availableSpots.put(type, 0);
        }
    }

    public void addParkingSpot(ParkingSpot spot) {
        ParkingSpotType type = spot.getType();
        parkingSpots.get(type).add(spot);
        availableSpots.put(type, availableSpots.get(type) + 1);
    }

    public ParkingSpot findAvailableSpot(Vehicle vehicle) {
        List<ParkingSpotType> preferredSpots = getPreferredSpotTypes(vehicle.getType());

        for (ParkingSpotType spotType : preferredSpots) {
            List<ParkingSpot> spots = parkingSpots.get(spotType);
            for (ParkingSpot spot : spots) {
                if (spot.isFree() && spot.canFitVehicle(vehicle)) {
                    return spot;
                }
            }
        }
        return null;
    }

    private List<ParkingSpotType> getPreferredSpotTypes(VehicleType vehicleType) {
        List<ParkingSpotType> preferred = new ArrayList<>();
        switch (vehicleType) {
            case BIKE:
                preferred.add(ParkingSpotType.SMALL);
                preferred.add(ParkingSpotType.MEDIUM);
                break;
            case CAR:
                preferred.add(ParkingSpotType.MEDIUM);
                break;
        }
        return preferred;
    }

    public void updateAvailableSpots(ParkingSpot spot, boolean isParking) {
        ParkingSpotType type = spot.getType();
        int current = availableSpots.get(type);
        availableSpots.put(type, isParking ? current - 1 : current + 1);
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public int getAvailableSpots(ParkingSpotType type) {
        return availableSpots.get(type);
    }
}