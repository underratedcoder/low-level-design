package com.lld.vendingmachine.state;

import com.lld.vendingmachine.VendingMachine;

public class ProductSelectionState implements VendingMachineState {
    VendingMachine machine;

    public ProductSelectionState(VendingMachine machine) {
        this.machine = machine;
    }

    public void pressInsertCoinButton() {
        System.out.println("Already past coin stage.");
    }

    public void insertCoin(int coin) {
        System.out.println("You cannot insert coins now.");
    }

    public void pressSelectProductButton() {
        System.out.println("Already in product selection.");
    }

    public void selectProduct(int rackId) {
        int price = 20; // Dummy value, use rack price lookup in full model
        if (machine.getBalance() >= price) {
            System.out.println("Product selected. Dispensing...");
            machine.deductBalance(price);
            machine.setState(machine.getDispensingState());
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    public void cancelRequest() {
        System.out.println("Cancelling... Returning to Idle.");
        machine.resetBalance();
        machine.setState(machine.getIdleState());
    }

    public void collectProduct() {
        System.out.println("Product not dispensed yet.");
    }
}
