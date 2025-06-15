package com.lld.sample.elevator;

import com.lld.sample.elevator.model.Request;

import java.util.ArrayList;
import java.util.List;

class ElevatorDispatcher {
    private List<Elevator> elevators;
    
    public ElevatorDispatcher(List<Elevator> elevators) {
        this.elevators = new ArrayList<>(elevators);
    }
    
    // Find best elevator for external request
    public Elevator findBestElevator(Request request) {
        Elevator bestElevator = null;
        int minCost = Integer.MAX_VALUE;
        
        for (Elevator elevator : elevators) {
            int cost = elevator.calculateCost(request);
            if (cost < minCost) {
                minCost = cost;
                bestElevator = elevator;
            }
        }
        
        return bestElevator;
    }
    
    // Dispatch external request
    public boolean dispatchRequest(Request request) {
        Elevator bestElevator = findBestElevator(request);
        if (bestElevator != null) {
            bestElevator.addRequest(request);
            return true;
        }
        return false;
    }
    
    public List<Elevator> getElevators() {
        return new ArrayList<>(elevators);
    }
}