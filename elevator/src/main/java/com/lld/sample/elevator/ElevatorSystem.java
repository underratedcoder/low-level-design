

package com.lld.sample.elevator;

import com.lld.sample.elevator.enums.Direction;
import com.lld.sample.elevator.enums.ElevatorState;
import com.lld.sample.elevator.model.Request;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Individual Elevator class
public class ElevatorSystem {
    private Building building;
    private ElevatorDispatcher dispatcher;
    private List<Elevator> elevators;
    private boolean isRunning;
    private ExecutorService executorService;

    public ElevatorSystem(int numElevators, int numFloors, int elevatorCapacity) {
        this.elevators = new ArrayList<>();

        // Initialize elevators
        for (int i = 1; i <= numElevators; i++) {
            elevators.add(new Elevator(i, elevatorCapacity));
        }

        this.dispatcher = new ElevatorDispatcher(elevators);
        this.building = new Building(numFloors, dispatcher);

        // Set building reference for each elevator
        for (Elevator elevator : elevators) {
            elevator.setBuilding(building);
        }

        this.isRunning = false;
        this.executorService = Executors.newFixedThreadPool(numElevators + 1);
    }

    // Start the elevator system
    public void start() {
        if (isRunning) return;

        isRunning = true;

        // Start each elevator in its own thread
        for (Elevator elevator : elevators) {
            executorService.submit(() -> {
                while (isRunning) {
                    elevator.move();
                    try {
                        Thread.sleep(1000); // Simulate 1 second per floor
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }
    }

    // Stop the elevator system
    public void stop() {
        isRunning = false;
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    // Handle external elevator call
    public boolean requestElevator(int floor, Direction direction) {
        try {
            return building.callElevator(floor, direction);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid request: " + e.getMessage());
            return false;
        }
    }

    // Handle internal elevator request (from inside elevator)
    public boolean selectFloor(int elevatorId, int targetFloor) {
        if (elevatorId < 1 || elevatorId > elevators.size()) {
            return false;
        }

        Elevator elevator = elevators.get(elevatorId - 1);
        return elevator.addDestination(targetFloor);
    }

    // Get system status
    public void printStatus() {
        System.out.println("=== Elevator System Status ===");
        for (Elevator elevator : elevators) {
//            System.out.printf("Elevator %d: Floor %d, Direction %s, State %s, Load %d, Destinations %s%n",
//                    elevator.getId(),
//                    elevator.getCurrentFloor(),
//                    elevator.getCurrentDirection(),
//                    elevator.getState(),
//                    elevator.getCurrentLoad(),
//                    elevator.getDestinationFloors()
//            );
        }
        System.out.println("Pending floor requests: " + building.getAllActiveRequests());
        System.out.println("================================");
    }

    // Getters
    public List<Elevator> getElevators() { return new ArrayList<>(elevators); }
    public Building getBuilding() { return building; }
    public boolean isRunning() { return isRunning; }
}
