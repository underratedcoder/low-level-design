package com.lld.parking.strategy;

import com.lld.parking.depth.enums.PaymentMethod;

public class DefaultPaymentProcessor implements PaymentProcessor {
    @Override
    public boolean processPayment(double amount, PaymentMethod method) {
        // Simulate payment processing
        System.out.println("Processing payment of $" + amount + " via " + method);
        // In real implementation, integrate with payment gateway
        return true;
    }
}