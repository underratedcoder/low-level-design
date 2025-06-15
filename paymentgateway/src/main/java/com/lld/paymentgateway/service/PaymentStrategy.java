package com.lld.paymentgateway.service;

public interface PaymentStrategy {
    boolean processPayment(double amount);
}