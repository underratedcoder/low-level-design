package com.lld.atm.model;

import com.lld.atm.enums.ATMStateType;
import com.lld.atm.factory.ATMStateFactory;
import com.lld.atm.repository.IRepository;
import com.lld.atm.state.ATMState;

public class ATMContext {
    private ATMState currentState;
    private double transactionAmount;
    private String transactionType;
    private int failedPinAttempts;
    
    // Repository dependencies
    private IRepository repository;
    
    // State factory
    private ATMStateFactory stateFactory;
    
    public ATMContext(IRepository repository) {
        this.repository = repository;
        this.stateFactory = new ATMStateFactory(this);
        this.currentState = stateFactory.createState(ATMStateType.IDLE);
        this.failedPinAttempts = 0;
    }
    
    // State management
    public void setState(ATMStateType stateType) {
        this.currentState = stateFactory.createState(stateType);
    }
    
    // Delegate methods to current state
    public void insertCard(String cardNumber) {
        currentState.insertCard(cardNumber);
    }
    
    public void enterPin(String pin) {
        currentState.enterPin(pin);
    }
    
    public void selectTransaction(String transactionType) {
        currentState.selectTransaction(transactionType);
    }
    
    public void enterAmount(double amount) {
        currentState.enterAmount(amount);
    }
    
    public void dispenseCash() {
        currentState.dispenseCash();
    }
    
    public void ejectCard() {
        currentState.ejectCard();
    }
    
    public String getBalance() {
        return currentState.getBalance();
    }
    
    // Getters and setters
    public double getTransactionAmount() { return transactionAmount; }
    public void setTransactionAmount(double transactionAmount) { this.transactionAmount = transactionAmount; }
    
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    
    public int getFailedPinAttempts() { return failedPinAttempts; }
    public void incrementFailedPinAttempts() { this.failedPinAttempts++; }
    public void resetFailedPinAttempts() { this.failedPinAttempts = 0; }
    
    public IRepository getRepository() { return repository; }
}