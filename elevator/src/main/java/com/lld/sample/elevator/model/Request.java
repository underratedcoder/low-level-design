package com.lld.sample.elevator.model;

import com.lld.sample.elevator.enums.Direction;
import com.lld.sample.elevator.enums.RequestType;

import java.util.Objects;

public class Request {
    private int floor;           // Internal
    private Direction direction; // External
    private RequestType type;
    private long timestamp;
    
    public Request(int floor, Direction direction, RequestType type) {
        this.floor = floor;
        this.direction = direction;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public int getFloor() { return floor; }
    public Direction getDirection() { return direction; }
    public RequestType getType() { return type; }
    public long getTimestamp() { return timestamp; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Request request = (Request) obj;
        return floor == request.floor && direction == request.direction;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(floor, direction);
    }
}