package com.lld.sample.elevator;

import com.lld.sample.elevator.enums.Direction;
import com.lld.sample.elevator.enums.ElevatorState;
import com.lld.sample.elevator.model.Request;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Main Elevator System Controller
class Elevator {
    private int id;
    private int currentFloor;
    private Direction currentDirection;
    private ElevatorState state;
    private Set<Integer> destinationFloors;
    private Queue<Request> pendingRequests;
    private int capacity;
    private int currentLoad;

    public Elevator(int id, int capacity) {
        this.id = id;
        this.currentFloor = 1; // Start at ground floor
        this.currentDirection = Direction.IDLE;
        this.state = ElevatorState.IDLE;
        this.capacity = capacity;
        this.currentLoad = 0;
        this.destinationFloors = new TreeSet<>();
        this.pendingRequests = new LinkedList<>();
    }

    // Add internal request (from inside elevator)
    public boolean addDestination(int floor) {
        if (floor == currentFloor || currentLoad >= capacity) {
            return false;
        }
        destinationFloors.add(floor);
        updateDirection();
        return true;
    }

    // Add external request
    public void addRequest(Request request) {
        pendingRequests.offer(request);
        destinationFloors.add(request.getFloor());
        updateDirection();
    }

    // Update elevator direction based on destinations
    private void updateDirection() {
        if (destinationFloors.isEmpty()) {
            currentDirection = Direction.IDLE;
            state = ElevatorState.IDLE;
            return;
        }

        boolean hasUpperFloor = destinationFloors.stream().anyMatch(f -> f > currentFloor);
        boolean hasLowerFloor = destinationFloors.stream().anyMatch(f -> f < currentFloor);

        if (currentDirection == Direction.UP || currentDirection == Direction.IDLE) {
            if (hasUpperFloor) {
                currentDirection = Direction.UP;
            } else if (hasLowerFloor) {
                currentDirection = Direction.DOWN;
            } else {
                currentDirection = Direction.IDLE;
            }
        } else if (currentDirection == Direction.DOWN) {
            if (hasLowerFloor) {
                currentDirection = Direction.DOWN;
            } else if (hasUpperFloor) {
                currentDirection = Direction.UP;
            } else {
                currentDirection = Direction.IDLE;
            }
        }

        state = (currentDirection != Direction.IDLE) ? ElevatorState.MOVING : ElevatorState.IDLE;
    }

    // Move elevator one floor
    public void move() {
        if (state != ElevatorState.MOVING || currentDirection == Direction.IDLE) {
            return;
        }

        if (currentDirection == Direction.UP) {
            currentFloor++;
        } else if (currentDirection == Direction.DOWN) {
            currentFloor--;
        }

        // Check if we need to stop at current floor
        if (shouldStop()) {
            stop();
        }

        updateDirection();
    }

    // Check if elevator should stop at current floor
    private boolean shouldStop() {
        return destinationFloors.contains(currentFloor);
    }

    // Stop at current floor
    private void stop() {
        destinationFloors.remove(currentFloor);

        // Remove matching pending requests and notify building
        pendingRequests.removeIf(req -> {
            if (req.getFloor() == currentFloor &&
                    (req.getDirection() == currentDirection || currentDirection == Direction.IDLE)) {
                // Notify building to clear the external request
                if (building != null) {
                    building.clearRequest(currentFloor, req.getDirection());
                }
                return true;
            }
            return false;
        });

        // Simulate door operations
        openDoors();
        closeDoors();
    }

    // Set building reference for clearing external requests
    private Building building;
    public void setBuilding(Building building) {
        this.building = building;
    }

    private void openDoors() {
        System.out.println("Elevator " + id + " doors opening at floor " + currentFloor);
    }

    private void closeDoors() {
        System.out.println("Elevator " + id + " doors closing at floor " + currentFloor);
    }

    // Calculate cost for handling a request (used by dispatcher)
    public int calculateCost(Request request) {
        if (state == ElevatorState.MAINTENANCE) {
            return Integer.MAX_VALUE;
        }

        int distance = Math.abs(currentFloor - request.getFloor());

        // Add penalty if elevator is moving in opposite direction
        if (currentDirection != Direction.IDLE &&
                currentDirection != request.getDirection() &&
                ((request.getFloor() > currentFloor && currentDirection == Direction.DOWN) ||
                        (request.getFloor() < currentFloor && currentDirection == Direction.UP))) {
            distance += 10;
        }

        // Add penalty based on current load
        distance += currentLoad;

        return distance;
    }

    // Getters
    public Object getId() { return id; }
    public int getCurrentFloor() { return currentFloor; }
    public Direction getCurrentDirection() { return currentDirection; }
    public ElevatorState getState() { return state; }
    public int getCurrentLoad() { return currentLoad; }
    public Set<Integer> getDestinationFloors() { return new HashSet<>(destinationFloors); }

    // For passenger entry/exit
    public boolean addPassenger() {
        if (currentLoad < capacity) {
            currentLoad++;
            return true;
        }
        return false;
    }

    public boolean removePassenger() {
        if (currentLoad > 0) {
            currentLoad--;
            return true;
        }
        return false;
    }

    public void setMaintenance(boolean maintenance) {
        if (maintenance) {
            state = ElevatorState.MAINTENANCE;
            currentDirection = Direction.IDLE;
        } else {
            state = ElevatorState.IDLE;
        }
    }
}