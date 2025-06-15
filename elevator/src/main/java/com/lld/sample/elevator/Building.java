package com.lld.sample.elevator;

import com.lld.sample.elevator.enums.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Building {
    private int totalFloors;
    private Map<Integer, ExternalControlPanel> floorPanels;

    public Building(int totalFloors, ElevatorDispatcher dispatcher) {
        this.totalFloors = totalFloors;
        this.floorPanels = new HashMap<>();

        // Install control panels on each floor
        for (int floor = 1; floor <= totalFloors; floor++) {
            floorPanels.put(floor, new ExternalControlPanel(floor, dispatcher));
        }
    }

    // Get control panel for a specific floor
    public ExternalControlPanel getControlPanel(int floor) {
        return floorPanels.get(floor);
    }

    // Call elevator from a floor (delegates to control panel)
    public boolean callElevator(int floor, Direction direction) {
        if (floor < 1 || floor > totalFloors) {
            throw new IllegalArgumentException("Invalid floor: " + floor);
        }

        ExternalControlPanel panel = floorPanels.get(floor);
        return panel != null && panel.pressButton(direction);
    }

    // Clear request when elevator serves a floor
    public void clearRequest(int floor, Direction direction) {
        ExternalControlPanel panel = floorPanels.get(floor);
        if (panel != null) {
            panel.clearRequest(direction);
        }
    }

    // Get all active floor requests (for status display)
    public Map<Integer, Set<Direction>> getAllActiveRequests() {
        Map<Integer, Set<Direction>> allRequests = new HashMap<>();
        for (Map.Entry<Integer, ExternalControlPanel> entry : floorPanels.entrySet()) {
            Set<Direction> requests = entry.getValue().getActiveRequests();
            if (!requests.isEmpty()) {
                allRequests.put(entry.getKey(), requests);
            }
        }
        return allRequests;
    }

    public int getTotalFloors() { return totalFloors; }
    public Map<Integer, ExternalControlPanel> getFloorPanels() {
        return new HashMap<>(floorPanels);
    }
}
