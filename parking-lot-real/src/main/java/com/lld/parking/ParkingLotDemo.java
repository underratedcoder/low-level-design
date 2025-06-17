package com.lld.parking;

import com.lld.parking.depth.enums.ParkingSpotType;
import com.lld.parking.depth.enums.PaymentMethod;
import com.lld.parking.depth.model.*;
import com.lld.parking.depth.model.vehicle.Bike;
import com.lld.parking.depth.model.vehicle.Car;
import com.lld.parking.depth.model.vehicle.Vehicle;
import com.lld.parking.depth.service.ParkingLot;
import com.lld.parking.strategy.GroundFloorPriorityStrategy;
import com.lld.parking.strategy.NearestToEntryStrategy;

public class ParkingLotDemo {
    public static void main(String[] args) {
        // Create parking lot
        ParkingLot parkingLot = new ParkingLot("PL001");
        
        // Create floors and add parking spots
        for (int floorNum = 1; floorNum <= 3; floorNum++) {
            ParkingFloor floor = new ParkingFloor(floorNum);
            
            // Add different types of parking spots
            for (int i = 1; i <= 5; i++) {
                floor.addParkingSpot(new ParkingSpot("F" + floorNum + "-M" + i,
                    ParkingSpotType.SMALL, floorNum));
            }
            for (int i = 1; i <= 10; i++) {
                floor.addParkingSpot(new ParkingSpot("F" + floorNum + "-C" + i, 
                    ParkingSpotType.MEDIUM, floorNum));
            }
            
            parkingLot.addFloor(floor);
        }
        
        // Add entry and exit points
        parkingLot.addEntryPoint(new EntryPoint("ENTRY1", parkingLot));
        parkingLot.addEntryPoint(new EntryPoint("ENTRY2", parkingLot));
        parkingLot.addExitPoint(new ExitPoint("EXIT1", parkingLot));
        parkingLot.addExitPoint(new ExitPoint("EXIT2", parkingLot));
        
        // Display initial status
        parkingLot.displayCurrentStatus();
        
        // Test different allocation strategies
        System.out.println("\n=== Testing Different Allocation Strategies ===");
        
        // Test 1: Default Random Strategy
        System.out.println("\n--- Test 1: Random Allocation ---");
        Vehicle car1 = new Car("ABC123");
        ParkingTicket ticket1 = parkingLot.parkVehicle(car1);
        
        // Test 2: Ground Floor Priority Strategy
        System.out.println("\n--- Test 2: Ground Floor Priority ---");
        parkingLot.setAllocationStrategy(new GroundFloorPriorityStrategy());
        Vehicle car2 = new Car("DEF456");
        ParkingTicket ticket2 = parkingLot.parkVehicle(car2);
        
        // Test 3: Nearest to Entry Strategy
        System.out.println("\n--- Test 3: Nearest to Entry ---");
        parkingLot.setAllocationStrategy(new NearestToEntryStrategy());
        Vehicle motorcycle1 = new Bike("XYZ789");
        ParkingTicket ticket3 = parkingLot.parkVehicle(motorcycle1);
        
        // Test 4: VIP Strategy
        System.out.println("\n--- Test 4: VIP Allocation ---");
        parkingLot.setAllocationStrategy(new VIPAllocationStrategy());
        Vehicle truck1 = new Truck("VIP001");
        ParkingTicket ticket4 = parkingLot.parkVehicle(truck1);
        
        // Display status after parking
        parkingLot.displayCurrentStatus();
        
        // Test strategy switching during operation
        System.out.println("\n--- Switching Strategy During Operation ---");
        parkingLot.setAllocationStrategy(new GroundFloorPriorityStrategy());
        Vehicle van1 = new Van("VAN123");
        ParkingTicket ticket5 = parkingLot.parkVehicle(van1);
        
        // Simulate some time passing and then unpark
        System.out.println("\n--- Testing Vehicle Exit ---");
        if (ticket1 != null) {
            parkingLot.unparkVehicle(ticket1, PaymentMethod.CASH);
        }
        
        // Display final status
        parkingLot.displayCurrentStatus();
        
        // Demonstrate strategy benefits
        demonstrateStrategyBenefits();
    }
    
    private static void demonstrateStrategyBenefits() {
        System.out.println("\n=== Strategy Pattern Benefits ===");
        System.out.println("1. **Flexibility**: Can switch allocation strategies at runtime");
        System.out.println("2. **Extensibility**: Easy to add new allocation strategies without modifying existing code");
        System.out.println("3. **Customization**: Different strategies for different customer types (VIP, regular, etc.)");
        System.out.println("4. **Business Logic Separation**: Allocation logic is separated from core parking operations");
        System.out.println("5. **Testability**: Each strategy can be tested independently");
        
        System.out.println("\n**Available Strategies:**");
        System.out.println("- RandomAllocationStrategy: First available spot (fastest)");
        System.out.println("- GroundFloorPriorityStrategy: Prefers ground floor for convenience");
        System.out.println("- NearestToEntryStrategy: Minimizes walking distance");
        System.out.println("- VIPAllocationStrategy: Best spots for premium customers");
        
        System.out.println("\n**Future Extensions:**");
        System.out.println("- DisabilityFriendlyStrategy: Handicapped spots priority");
        System.out.println("- ElectricVehicleStrategy: Near charging stations");
        System.out.println("- CompactCarStrategy: Optimize space utilization");
        System.out.println("- TimeBasedStrategy: Different allocation during peak/off-peak hours");
    }
}