package com.lld.vendingmachine;

public class Account {
    int collectedCash = 0;

    public void addCash(int amount) {
        collectedCash += amount;
    }

    public int getCollectedCash() {
        return collectedCash;
    }
}
