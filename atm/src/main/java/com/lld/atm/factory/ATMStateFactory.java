package com.lld.atm.factory;

import com.lld.atm.enums.ATMStateType;
import com.lld.atm.model.ATMContext;
import com.lld.atm.state.*;

public class ATMStateFactory {
    private ATMContext context;
    
    public ATMStateFactory(ATMContext context) {
        this.context = context;
    }
    
    public ATMState createState(ATMStateType stateType) {
        switch (stateType) {
            case IDLE:
                return new IdleState(context);
            case CARD_INSERTED:
                return new CardInsertedState(context);
            case PIN_ENTERED:
                return new PinEnteredState(context);
            case TRANSACTION_SELECTION:
                return new TransactionSelectionState(context);
            case CASH_DISPENSING:
                return new CashDispensingState(context);
            default:
                throw new IllegalArgumentException("Unknown state type: " + stateType);
        }
    }
}