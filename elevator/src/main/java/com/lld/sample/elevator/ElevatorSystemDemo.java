package com.lld.sample.elevator;

import com.lld.sample.elevator.enums.Direction;

class ElevatorSystemDemo {
    public static void main(String[] args) throws InterruptedException {
        // Create elevator system with 3 elevators, 10 floors, capacity 8 people each
        ElevatorSystem elevatorSystem = new ElevatorSystem(3, 10, 8);
        
        // Start the system
        elevatorSystem.start();
        
        // Simulate some requests
        System.out.println("Starting elevator system demo...");
        
        // External requests (people calling elevators)
        elevatorSystem.requestElevator(5, Direction.UP);
        elevatorSystem.requestElevator(3, Direction.DOWN);
        elevatorSystem.requestElevator(8, Direction.DOWN);
        
        Thread.sleep(2000);
        elevatorSystem.printStatus();
        
        // Internal requests (people selecting floors inside elevators)
        elevatorSystem.selectFloor(1, 7);
        elevatorSystem.selectFloor(2, 1);
        elevatorSystem.selectFloor(3, 6);
        
        Thread.sleep(3000);
        elevatorSystem.printStatus();
        
        // More requests
        elevatorSystem.requestElevator(9, Direction.DOWN);
        elevatorSystem.requestElevator(2, Direction.UP);
        
        Thread.sleep(5000);
        elevatorSystem.printStatus();
        
        // Stop the system
        elevatorSystem.stop();
        System.out.println("Elevator system stopped.");
    }
}