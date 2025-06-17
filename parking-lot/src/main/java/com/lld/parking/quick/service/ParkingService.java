package com.lld.parking.quick.service;

import com.lld.parking.quick.model.PaymentReceipt;
import com.lld.parking.quick.enums.PaymentMethod;
import com.lld.parking.quick.enums.PaymentStatus;
import com.lld.parking.quick.enums.VehicleType;
import com.lld.parking.quick.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingService {
    private final Map<Integer, Floor> floors = new HashMap<>();
    private final Map<String, ParkingTicket> activeTickets = new HashMap<>();
    private final Map<VehicleType, Double> hourlyRates = new HashMap<>();
    private int ticketCounter = 1;

    public ParkingService(List<Floor> floorList) {
        for (Floor floor : floorList) {
            floors.put(floor.getFloorNumber(), floor);
        }
        hourlyRates.put(VehicleType.CAR, 20.0);
        hourlyRates.put(VehicleType.BIKE, 10.0);
        hourlyRates.put(VehicleType.TRUCK, 40.0);
    }

    public ParkingTicket parkVehicle(Vehicle vehicle, EntryGate gate) {
        for (Floor floor : floors.values()) {
            List<ParkingSpot> spots = floor.getSpotsByType().get(vehicle.getType());
            for (ParkingSpot spot : spots) {
                if (!spot.isOccupied()) {
                    spot.setOccupied(true);
                    spot.setParkedVehicle(vehicle);
                    String ticketId = "TICKET-" + ticketCounter++;
                    ParkingTicket ticket = new ParkingTicket(ticketId, vehicle, LocalDateTime.now(), floor.getFloorNumber(), spot.getSpotId());
                    activeTickets.put(ticketId, ticket);
                    return ticket;
                }
            }
        }
        throw new RuntimeException("No available spots for vehicle type: " + vehicle.getType());
    }

    public PaymentReceipt exitVehicle(String ticketId, ExitGate gate, PaymentMethod method) {
        ParkingTicket ticket = activeTickets.get(ticketId);
        if (ticket == null) throw new RuntimeException("Invalid Ticket ID");

        Floor floor = floors.get(ticket.getFloorNumber());
        List<ParkingSpot> spots = floor.getSpotsByType().get(ticket.getVehicle().getType());
        ParkingSpot spot = spots.stream()
                .filter(s -> s.getSpotId() == ticket.getSpotId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Spot not found"));

        Duration duration = Duration.between(ticket.getEntryTime(), LocalDateTime.now());
        double hours = Math.ceil(duration.toMinutes() / 60.0);
        double amount = hours * hourlyRates.get(ticket.getVehicle().getType());

        // free the spot
        spot.setOccupied(false);
        spot.setParkedVehicle(null);

        activeTickets.remove(ticketId);

        return new PaymentReceipt(ticketId, amount, method, LocalDateTime.now(), PaymentStatus.SUCCESS);
    }
}