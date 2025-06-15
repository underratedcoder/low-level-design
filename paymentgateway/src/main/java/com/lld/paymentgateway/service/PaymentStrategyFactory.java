package com.lld.paymentgateway.service;

import com.lld.paymentgateway.enums.PaymentMethodType;

public class PaymentStrategyFactory {
    public static PaymentStrategy getPaymentStrategy(PaymentMethodType type) {
        switch (type) {
            case CARD:
                return new CardPayment();
            case UPI:
                return new UpiPayment();
            default:
                throw new IllegalArgumentException("Invalid payment method");
        }
    }
}