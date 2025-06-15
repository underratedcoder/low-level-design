package com.lld.vendingmachine.state;

import com.lld.vendingmachine.VendingMachine;

public class DispensingState implements VendingMachineState {
    VendingMachine machine;

    public DispensingState(VendingMachine machine) {
        this.machine = machine;
    }

    public void pressInsertCoinButton() {
        System.out.println("Wait! Dispensing in progress.");
    }

    public void insertCoin(int coin) {
        System.out.println("Wait! Dispensing in progress.");
    }

    public void pressSelectProductButton() {
        System.out.println("Wait! Dispensing in progress.");
    }

    public void selectProduct(int rackId) {
        System.out.println("Already dispensing.");
    }

    public void cancelRequest() {
        System.out.println("Too late to cancel.");
    }

    public void collectProduct() {
        System.out.println("Product collected. Returning to Idle.");
        machine.setState(machine.getIdleState());
    }
}
