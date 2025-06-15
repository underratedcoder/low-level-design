package com.lld.vendingmachine;

public class Spot {
    int id;
    SpotStatus status;
    Item item;

    public Spot(int id, Item item) {
        this.id = id;
        this.item = item;
        this.status = (item == null) ? SpotStatus.EMPTY : SpotStatus.OCCUPIED;
    }
}
