package com.lld.atm.state;

import com.lld.atm.enums.ATMStateType;
import com.lld.atm.model.ATMContext;

public class TransactionSelectionState extends ATMState {
    public TransactionSelectionState(ATMContext context) {
        super(context);
    }
    
    @Override
    public void insertCard(String cardNumber) {
        System.out.println("Transaction in progress.");
    }
    
    @Override
    public void enterPin(String pin) {
        System.out.println("Transaction in progress.");
    }
    
    @Override
    public void selectTransaction(String transactionType) {
        System.out.println("Transaction already selected. Please enter amount.");
    }
    
    @Override
    public void enterAmount(double amount) {
        context.setTransactionAmount(amount);
        
        if ("WITHDRAWAL".equals(context.getTransactionType())) {
            if (context.getCurrentAccount().getBalance() >= amount) {
                context.setState(ATMStateType.CASH_DISPENSING);
                System.out.println("Processing withdrawal...");
                dispenseCash();
            } else {
                System.out.println("Insufficient funds. Available balance: $" + 
                    context.getCurrentAccount().getBalance());
                ejectCard();
            }
        } else if ("DEPOSIT".equals(context.getTransactionType())) {
            context.getCurrentAccount().deposit(amount);
            context.getAccountRepository().updateAccount(context.getCurrentAccount());
            
            // Record transaction
            Transaction transaction = new Transaction(
                "TXN" + System.currentTimeMillis(),
                context.getCurrentAccount().getAccountNumber(),
                "DEPOSIT",
                amount
            );
            context.getTransactionRepository().saveTransaction(transaction);
            
            System.out.println("Deposit successful. New balance: $" + 
                context.getCurrentAccount().getBalance());
            ejectCard();
        }
    }
    
    @Override
    public void dispenseCash() {
        System.out.println("Please enter amount first.");
    }
    
    @Override
    public void ejectCard() {
        context.setCurrentCard(null);
        context.setCurrentAccount(null);
        context.setTransactionAmount(0);
        context.setTransactionType(null);
        context.setState(ATMStateType.IDLE);
        System.out.println("Transaction cancelled. Card ejected.");
    }
    
    @Override
    public String getBalance() {
        return "Current Balance: $" + context.getCurrentAccount().getBalance();
    }
}