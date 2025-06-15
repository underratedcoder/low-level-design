package com.lld.atm.state;

import com.lld.atm.enums.ATMStateType;
import com.lld.atm.model.ATMContext;

public class CardInsertedState extends ATMState {
    public CardInsertedState(ATMContext context) {
        super(context);
    }
    
    @Override
    public void insertCard(String cardNumber) {
        System.out.println("Card already inserted.");
    }
    
    @Override
    public void enterPin(String pin) {
        Card currentCard = context.getCurrentCard();
        if (currentCard.getPin().equals(pin)) {
            context.resetFailedPinAttempts();
            Account account = context.getAccountRepository()
                    .findByAccountNumber(currentCard.getAccountNumber());
            context.setCurrentAccount(account);
            context.setState(ATMStateType.PIN_ENTERED);
            System.out.println("PIN verified. Welcome " + account.getAccountHolderName());
        } else {
            context.incrementFailedPinAttempts();
            if (context.getFailedPinAttempts() >= 3) {
                currentCard.setBlocked(true);
                context.getCardRepository().updateCard(currentCard);
                System.out.println("Card blocked due to multiple failed attempts.");
                ejectCard();
            } else {
                System.out.println("Incorrect PIN. Attempts remaining: " + 
                    (3 - context.getFailedPinAttempts()));
            }
        }
    }
    
    @Override
    public void selectTransaction(String transactionType) {
        System.out.println("Please enter PIN first.");
    }
    
    @Override
    public void enterAmount(double amount) {
        System.out.println("Please enter PIN first.");
    }
    
    @Override
    public void dispenseCash() {
        System.out.println("Please enter PIN first.");
    }
    
    @Override
    public void ejectCard() {
        context.setCurrentCard(null);
        context.setState(ATMStateType.IDLE);
        System.out.println("Card ejected.");
    }
    
    @Override
    public String getBalance() {
        return "Please enter PIN first.";
    }
}