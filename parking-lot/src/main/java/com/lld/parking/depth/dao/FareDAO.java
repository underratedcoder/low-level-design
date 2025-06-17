package com.lld.parking.depth.dao;

import com.lld.parking.depth.model.Fare;

import java.util.ArrayList;
import java.util.List;

public class FareDAO {
    private List<Fare> fares = new ArrayList<>();

    public void createFare(String bookingId, double fareAmount) {
        String fareId = "FARE" + (fares.size() + 1);
        Fare fare = new Fare(fareId, bookingId, fareAmount);
        fares.add(fare);
    }
}