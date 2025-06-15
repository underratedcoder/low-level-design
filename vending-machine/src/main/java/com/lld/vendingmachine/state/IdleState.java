package com.lld.vendingmachine.state;

import com.lld.vendingmachine.VendingMachine;

public class IdleState implements VendingMachineState {
    VendingMachine machine;

    public IdleState(VendingMachine machine) {
        this.machine = machine;
    }

    public void pressInsertCoinButton() {
        System.out.println("Transitioning to AcceptingCoinState.");
        machine.setState(machine.getAcceptingCoinState());
    }

    public void insertCoin(int coin) {
        System.out.println("Press insert coin button first.");
    }

    public void pressSelectProductButton() {
        System.out.println("Insert coins first.");
    }

    public void selectProduct(int rackId) {
        System.out.println("Insert coins first.");
    }

    public void cancelRequest() {
        System.out.println("Nothing to cancel.");
    }

    public void collectProduct() {
        System.out.println("No product to collect.");
    }
}
