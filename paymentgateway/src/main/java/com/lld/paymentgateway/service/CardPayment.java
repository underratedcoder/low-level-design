package com.lld.paymentgateway.service;

public class CardPayment implements PaymentStrategy {
    public boolean processPayment(double amount) {
        System.out.println("Processing card payment of " + amount);
        return true;
    }
}