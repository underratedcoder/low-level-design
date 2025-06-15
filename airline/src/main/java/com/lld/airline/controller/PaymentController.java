package com.lld.airline.controller;


import com.lld.airline.model.Payment;
import com.lld.airline.service.impl.PaymentManager;

public class PaymentController {
    private final PaymentManager paymentManager = PaymentManager.getInstance();

    /**
     * User proceed to pay, he will see a payment gateway page which will be having context of payment
     * */
    public void initiatePayment(Long bookingId, double amount) {
        paymentManager.initiatePayment(bookingId, amount);
    }

    public void paymentStatus(Long paymentId, Payment payment) {
        paymentManager.updatePayment(paymentId, payment);
    }
}
