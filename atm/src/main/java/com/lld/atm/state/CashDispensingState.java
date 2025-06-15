package com.lld.atm.state;

import com.lld.atm.model.ATMContext;

public class CashDispensingState extends ATMState {
    public CashDispensingState(ATMContext context) {
        super(context);
    }
    
    @Override
    public void insertCard(String cardNumber) {
        System.out.println("Cash dispensing in progress.");
    }
    
    @Override
    public void enterPin(String pin) {
        System.out.println("Cash dispensing in progress.");
    }
    
    @Override
    public void selectTransaction(String transactionType) {
        System.out.println("Cash dispensing in progress.");
    }
    
    @Override
    public void enterAmount(double amount) {
        System.out.println("Cash dispensing in progress.");
    }
    
    @Override
    public void dispenseCash() {
        Account account = context.getCurrentAccount();
        double amount = context.getTransactionAmount();
        
        if (account.withdraw(amount)) {
            context.getAccountRepository().updateAccount(account);
            
            // Record transaction
            Transaction transaction = new Transaction(
                "TXN" + System.currentTimeMillis(),
                account.getAccountNumber(),
                "WITHDRAWAL",
                amount
            );
            context.getTransactionRepository().saveTransaction(transaction);
            
            System.out.println("Cash dispensed: $" + amount);
            System.out.println("Remaining balance: $" + account.getBalance());
            System.out.println("Please collect your cash and card.");
            
            ejectCard();
        } else {
            System.out.println("Unable to dispense cash. Please try again.");
            ejectCard();
        }
    }