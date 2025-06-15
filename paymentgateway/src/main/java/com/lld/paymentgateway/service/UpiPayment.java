package com.lld.paymentgateway.service;

public class UpiPayment implements PaymentStrategy {
    public boolean processPayment(double amount) {
        System.out.println("Processing UPI payment of " + amount);
        return true;
    }
}