package com.lld.vendingmachine.state;

public interface VendingMachineState {
    void pressInsertCoinButton();
    void insertCoin(int coin);
    void pressSelectProductButton();
    void selectProduct(int rackId);
    void cancelRequest();
    void collectProduct();
}
