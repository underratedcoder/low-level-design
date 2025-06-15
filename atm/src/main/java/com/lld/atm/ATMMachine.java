package com.lld.atm;

import com.lld.atm.model.ATMContext;
import com.lld.atm.repository.IRepository;
import com.lld.atm.repository.Repository;

class ATMMachine {
    private ATMContext context;
    
    public ATMMachine() {
        // Initialize repositories
       IRepository repository = new Repository();
        // Initialize ATM context
        this.context = new ATMContext(repository);
    }
    
    public void insertCard(String cardNumber) {
        context.insertCard(cardNumber);
    }
    
    public void enterPin(String pin) {
        context.enterPin(pin);
    }
    
    public void selectTransaction(String transactionType) {
        context.selectTransaction(transactionType);
    }
    
    public void enterAmount(double amount) {
        context.enterAmount(amount);
    }
    
    public void dispenseCash() {
        context.dispenseCash();
    }
    
    public void ejectCard() {
        context.ejectCard();
    }
    
    public String checkBalance() {
        return context.getBalance();
    }
}