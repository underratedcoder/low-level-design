package com.lld.sample.elevator;

import com.lld.sample.elevator.enums.Direction;
import com.lld.sample.elevator.enums.RequestType;
import com.lld.sample.elevator.model.Request;

import java.util.HashSet;
import java.util.Set;

// External Control Panel - installed on each floor to handle elevator calls
public class ExternalControlPanel {
    private int floorNumber;
    private Set<Direction> activeRequests;
    private ElevatorDispatcher dispatcher;
    
    public ExternalControlPanel(int floorNumber, ElevatorDispatcher dispatcher) {
        this.floorNumber = floorNumber;
        this.activeRequests = new HashSet<>();
        this.dispatcher = dispatcher;
    }
    
    // Press up/down button on the floor
    public boolean pressButton(Direction direction) {
        if (direction == Direction.IDLE) {
            return false;
        }
        
        // Don't create duplicate requests
        if (activeRequests.contains(direction)) {
            return false;
        }
        
        Request request = new Request(floorNumber, direction, RequestType.EXTERNAL);
        boolean success = dispatcher.dispatchRequest(request);
        
        if (success) {
            activeRequests.add(direction);
            System.out.println("Floor " + floorNumber + " - " + direction + " button pressed");
        }
        
        return success;
    }
    
    // Clear request when elevator arrives and serves this floor/direction
    public void clearRequest(Direction direction) {
        if (activeRequests.remove(direction)) {
            System.out.println("Floor " + floorNumber + " - " + direction + " request cleared");
        }
    }
    
    // Check if button is currently active (lit up)
    public boolean isButtonActive(Direction direction) {
        return activeRequests.contains(direction);
    }
    
    public int getFloorNumber() { return floorNumber; }
    public Set<Direction> getActiveRequests() { return new HashSet<>(activeRequests); }
}