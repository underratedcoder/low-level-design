package com.lld.vendingmachine;

import java.util.LinkedList;
import java.util.Queue;

public class Rack {
    int id;
    int noOfSpots;
    int emptySpots;
    Queue<Spot> spots;
    int itemPrice;

    public Rack(int id, int noOfSpots, int itemPrice) {
        this.id = id;
        this.noOfSpots = noOfSpots;
        this.emptySpots = noOfSpots;
        this.spots = new LinkedList<>();
        this.itemPrice = itemPrice;
    }

    public void addSpot(Spot spot) {
        spots.add(spot);
        if (spot.status == SpotStatus.OCCUPIED) {
            emptySpots--;
        }
    }

    public Spot dispenseItem() {
        if (!spots.isEmpty()) {
            Spot spot = spots.poll();
            if (spot != null && spot.status == SpotStatus.OCCUPIED) {
                emptySpots++;
                return spot;
            }
        }
        return null;
    }
}
