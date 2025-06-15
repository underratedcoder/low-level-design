package com.lld.atm.state;

import com.lld.atm.model.ATMContext;

public abstract class ATMState {
    protected ATMContext context;
    
    public ATMState(ATMContext context) {
        this.context = context;
    }
    
    public abstract void insertCard(String cardNumber);
    public abstract void enterPin(String pin);
    public abstract void selectTransaction(String transactionType);
    public abstract void enterAmount(double amount);
    public abstract void dispenseCash();
    public abstract void ejectCard();
    public abstract String getBalance();
}