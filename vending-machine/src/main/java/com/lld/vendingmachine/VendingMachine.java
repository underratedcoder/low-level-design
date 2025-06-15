package com.lld.vendingmachine;

import com.lld.vendingmachine.state.*;

public class VendingMachine {
    VendingMachineState idleState;
    VendingMachineState acceptingCoinState;
    VendingMachineState productSelectionState;
    VendingMachineState dispensingState;

    VendingMachineState currentState;

    int balance = 0;

    public VendingMachine() {
        idleState = new IdleState(this);
        acceptingCoinState = new AcceptingCoinState(this);
        productSelectionState = new ProductSelectionState(this);
        dispensingState = new DispensingState(this);

        currentState = idleState;
    }

    public void setState(VendingMachineState state) {
        this.currentState = state;
    }

    public VendingMachineState getIdleState() {
        return idleState;
    }

    public VendingMachineState getAcceptingCoinState() {
        return acceptingCoinState;
    }

    public VendingMachineState getProductSelectionState() {
        return productSelectionState;
    }

    public VendingMachineState getDispensingState() {
        return dispensingState;
    }

    public int getBalance() {
        return balance;
    }

    public void addBalance(int coin) {
        balance += coin;
    }

    public void deductBalance(int amount) {
        balance -= amount;
    }

    public void resetBalance() {
        balance = 0;
    }

    // Delegation methods
    public void pressInsertCoinButton() {
        currentState.pressInsertCoinButton();
    }

    public void insertCoin(int coin) {
        currentState.insertCoin(coin);
    }

    public void pressSelectProductButton() {
        currentState.pressSelectProductButton();
    }

    public void selectProduct(int rackId) {
        currentState.selectProduct(rackId);
    }

    public void cancelRequest() {
        currentState.cancelRequest();
    }

    public void collectProduct() {
        currentState.collectProduct();
    }
}
