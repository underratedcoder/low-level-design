package com.lld.parking.strategy;

import com.lld.parking.depth.enums.PaymentMethod;

public interface PaymentProcessor {
    boolean processPayment(double amount, PaymentMethod method);
}