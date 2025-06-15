package com.lld.atm.state;

import com.lld.atm.enums.ATMStateType;
import com.lld.atm.model.ATMContext;

public class PinEnteredState extends ATMState {
    public PinEnteredState(ATMContext context) {
        super(context);
    }
    
    @Override
    public void insertCard(String cardNumber) {
        System.out.println("Card already inserted and authenticated.");
    }
    
    @Override
    public void enterPin(String pin) {
        System.out.println("PIN already verified.");
    }
    
    @Override
    public void selectTransaction(String transactionType) {
        context.setTransactionType(transactionType);
        
        if ("BALANCE_INQUIRY".equals(transactionType)) {
            System.out.println("Current Balance: $" + context.getCurrentAccount().getBalance());
            ejectCard();
        } else if ("WITHDRAWAL".equals(transactionType) || "DEPOSIT".equals(transactionType)) {
            context.setState(ATMStateType.TRANSACTION_SELECTION);
            System.out.println("Please enter amount for " + transactionType);
        } else {
            System.out.println("Invalid transaction type.");
        }
    }
    
    @Override
    public void enterAmount(double amount) {
        System.out.println("Please select transaction type first.");
    }
    
    @Override
    public void dispenseCash() {
        System.out.println("Please select transaction first.");
    }
    
    @Override
    public void ejectCard() {
        context.setCurrentCard(null);
        context.setCurrentAccount(null);
        context.setState(ATMStateType.IDLE);
        System.out.println("Card ejected. Thank you!");
    }
    
    @Override
    public String getBalance() {
        return "Current Balance: $" + context.getCurrentAccount().getBalance();
    }
}