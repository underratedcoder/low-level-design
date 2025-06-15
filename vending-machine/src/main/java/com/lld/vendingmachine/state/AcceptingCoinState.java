package com.lld.vendingmachine.state;

import com.lld.vendingmachine.VendingMachine;

public class AcceptingCoinState implements VendingMachineState {
    VendingMachine machine;

    public AcceptingCoinState(VendingMachine machine) {
        this.machine = machine;
    }

    public void pressInsertCoinButton() {
        System.out.println("Already in coin accepting mode.");
    }

    public void insertCoin(int coin) {
        machine.addBalance(coin);
        System.out.println("Inserted coin: " + coin + ". Current balance: " + machine.getBalance());
    }

    public void pressSelectProductButton() {
        System.out.println("Transitioning to ProductSelectionState.");
        machine.setState(machine.getProductSelectionState());
    }

    public void selectProduct(int rackId) {
        System.out.println("Press select product button first.");
    }

    public void cancelRequest() {
        System.out.println("Cancelling... Returning to Idle.");
        machine.resetBalance();
        machine.setState(machine.getIdleState());
    }

    public void collectProduct() {
        System.out.println("No product to collect.");
    }
}
