package com.lld.atm.state;

import com.lld.atm.model.ATMContext;

public class IdleState extends ATMState {
    public IdleState(ATMContext context) {
        super(context);
    }
    
    @Override
    public void insertCard(String cardNumber) {
        Card card = context.findByCardNumber(cardNumber);
        if (card != null && !card.isBlocked()) {
            context.setCurrentCard(card);
            context.setState(ATMStateType.CARD_INSERTED);
            System.out.println("Card inserted successfully. Please enter PIN.");
        } else {
            System.out.println("Invalid or blocked card. Card ejected.");
        }
    }
    
    @Override
    public void enterPin(String pin) {
        System.out.println("Please insert card first.");
    }
    
    @Override
    public void selectTransaction(String transactionType) {
        System.out.println("Please insert card first.");
    }
    
    @Override
    public void enterAmount(double amount) {
        System.out.println("Please insert card first.");
    }
    
    @Override
    public void dispenseCash() {
        System.out.println("No transaction in progress.");
    }
    
    @Override
    public void ejectCard() {
        System.out.println("No card to eject.");
    }
    
    @Override
    public String getBalance() {
        return "Please insert card first.";
    }
}