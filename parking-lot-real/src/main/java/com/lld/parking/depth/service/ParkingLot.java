package com.lld.parking.depth.service;

import com.lld.parking.depth.enums.ParkingSpotType;
import com.lld.parking.depth.enums.ParkingTicketStatus;
import com.lld.parking.depth.enums.PaymentMethod;
import com.lld.parking.depth.enums.VehicleType;
import com.lld.parking.depth.model.*;
import com.lld.parking.depth.model.vehicle.Vehicle;
import com.lld.parking.strategy.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLot {
    private String parkingLotId;
    private List<ParkingFloor> floors;
    private List<EntryPoint> entryPoints;
    private List<ExitPoint> exitPoints;
    private Map<String, ParkingTicket> activeTickets;
    private PricingStrategy pricingStrategy;
    private PaymentProcessor paymentProcessor;
    private ParkingAllocationStrategy allocationStrategy;
    private Map<VehicleType, Integer> maxCapacity;
    private Map<VehicleType, Integer> currentOccupancy;
    
    public ParkingLot(String parkingLotId) {
        this.parkingLotId = parkingLotId;
        this.floors = new ArrayList<>();
        this.entryPoints = new ArrayList<>();
        this.exitPoints = new ArrayList<>();
        this.activeTickets = new HashMap<>();
        this.pricingStrategy = new HourlyPricingStrategy();
        this.paymentProcessor = new DefaultPaymentProcessor();
        this.allocationStrategy = new RandomAllocationStrategy(); // Default strategy
        this.maxCapacity = new HashMap<>();
        this.currentOccupancy = new HashMap<>();
        
        // Initialize occupancy tracking
        for (VehicleType type : VehicleType.values()) {
            currentOccupancy.put(type, 0);
        }
    }
    
    // Method to change allocation strategy
    public void setAllocationStrategy(ParkingAllocationStrategy strategy) {
        this.allocationStrategy = strategy;
        System.out.println("Allocation strategy updated to: " + strategy.getClass().getSimpleName());
    }
    
    public void addFloor(ParkingFloor floor) {
        floors.add(floor);
        updateCapacity();
    }
    
    public void addEntryPoint(EntryPoint entryPoint) {
        entryPoints.add(entryPoint);
    }
    
    public void addExitPoint(ExitPoint exitPoint) {
        exitPoints.add(exitPoint);
    }
    
    private void updateCapacity() {
        for (VehicleType vehicleType : VehicleType.values()) {
            int capacity = 0;
            for (ParkingFloor floor : floors) {
                switch (vehicleType) {
                    case BIKE:
                        capacity += floor.getAvailableSpots(ParkingSpotType.SMALL);
                        capacity += floor.getAvailableSpots(ParkingSpotType.MEDIUM);
                        break;
                    case CAR:
                        capacity += floor.getAvailableSpots(ParkingSpotType.MEDIUM);
                        break;
                }
            }
            maxCapacity.put(vehicleType, capacity);
        }
    }
    
    public boolean canPark(VehicleType vehicleType) {
        return currentOccupancy.get(vehicleType) < maxCapacity.get(vehicleType);
    }
    
    public ParkingTicket parkVehicle(Vehicle vehicle) {
        if (!canPark(vehicle.getType())) {
            System.out.println("Parking full for vehicle type: " + vehicle.getType());
            return null;
        }
        
        // Use allocation strategy to find the best spot
        ParkingSpot availableSpot = allocationStrategy.findParkingSpot(vehicle, floors, entryPoints);
        
        if (availableSpot != null) {
            // Park the vehicle
            if (availableSpot.assignVehicle(vehicle)) {
                // Update floor availability
                for (ParkingFloor floor : floors) {
                    if (floor.getFloorNumber() == availableSpot.getFloor()) {
                        floor.updateAvailableSpots(availableSpot, true);
                        break;
                    }
                }
                
                // Create and store ticket
                ParkingTicket ticket = new ParkingTicket(
                    vehicle.getLicensePlate(), 
                    vehicle.getType(), 
                    availableSpot
                );
                activeTickets.put(ticket.getTicketId(), ticket);
                
                // Update occupancy
                currentOccupancy.put(vehicle.getType(), 
                    currentOccupancy.get(vehicle.getType()) + 1);
                
                System.out.println("Vehicle parked successfully. Ticket ID: " + 
                    ticket.getTicketId());
                return ticket;
            }
        }
        
        System.out.println("No available parking spot found");
        return null;
    }
    
    public boolean unparkVehicle(ParkingTicket ticket, PaymentMethod paymentMethod) {
        if (!activeTickets.containsKey(ticket.getTicketId())) {
            System.out.println("Invalid ticket");
            return false;
        }
        
        // Calculate fare
        ticket.setExitTime(LocalDateTime.now());
        long hoursParked = Duration.between(ticket.getEntryTime(),
            ticket.getExitTime()).toHours();
        double fare = pricingStrategy.calculateFare(ticket.getVehicleType(), hoursParked);
        ticket.setAmount(fare);
        
        // Process payment
        if (paymentProcessor.processPayment(fare, paymentMethod)) {
            // Free up the parking spot
            ParkingSpot spot = ticket.getParkingSpot();
            spot.removeVehicle();
            
            // Update floor availability
            for (ParkingFloor floor : floors) {
                if (floor.getFloorNumber() == spot.getFloor()) {
                    floor.updateAvailableSpots(spot, false);
                    break;
                }
            }
            
            // Update occupancy
            currentOccupancy.put(ticket.getVehicleType(), 
                currentOccupancy.get(ticket.getVehicleType()) - 1);
            
            // Update ticket status
            ticket.setStatus(ParkingTicketStatus.PAID);
            activeTickets.remove(ticket.getTicketId());
            
            System.out.println("Vehicle unparked successfully. Total fare: $" + fare);
            return true;
        }
        
        System.out.println("Payment failed");
        return false;
    }
    
    public void displayCurrentStatus() {
        System.out.println("\n=== Parking Lot Status ===");
        System.out.println("Parking Lot ID: " + parkingLotId);
        System.out.println("Total Floors: " + floors.size());
        System.out.println("Entry Points: " + entryPoints.size());
        System.out.println("Exit Points: " + exitPoints.size());
        System.out.println("Active Tickets: " + activeTickets.size());
        
        System.out.println("\nOccupancy by Vehicle Type:");
        for (VehicleType type : VehicleType.values()) {
            System.out.println(type + ": " + currentOccupancy.get(type) + 
                "/" + maxCapacity.get(type));
        }
    }
    
    // Getters
    public String getParkingLotId() { return parkingLotId; }
    public List<ParkingFloor> getFloors() { return floors; }
    public Map<String, ParkingTicket> getActiveTickets() { return activeTickets; }
}
