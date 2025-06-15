package com.lld.airline.service.impl;

import com.lld.airline.dao.PaymentDAO;
import com.lld.airline.enums.PaymentMethodType;
import com.lld.airline.enums.PaymentStatus;
import com.lld.airline.model.Payment;
import com.lld.airline.service.notify.NotificationService;

import java.time.LocalDateTime;

public class PaymentManager {
    private static volatile PaymentManager instance;

    public static PaymentManager getInstance() {
        if (instance == null) {
            synchronized (PaymentManager.class) {
                if (instance == null) {
                    instance = new PaymentManager(
                            PaymentDAO.getInstance(),
                            NotificationService.getInstance()
                    );
                }
            }
        }
        return instance;
    }

    private final PaymentDAO paymentDAO;
    private final NotificationService notificationService;

    private PaymentManager(
            PaymentDAO paymentDAO,
            NotificationService notificationService
    ) {
            this.paymentDAO = paymentDAO;
            this.notificationService = notificationService;
    }

    /**
     *
     */
    public Payment initiatePayment(
            Long bookingId,
            double amount
    ) {

        try {
            Payment payment = new Payment();
            payment.setBookingId(bookingId);
            payment.setAmount(amount);
            payment.setCreatedDatetime(LocalDateTime.now());

            paymentDAO.save(payment);

            return payment;
        } catch (Exception e) {
            throw new RuntimeException("Booking initiation failed: " + e.getMessage());
        }
    }

    /**
     *
     * 1. COMPLETED          - Got successful from payment gateway                       (Sync Call)
     * 2. FAILED             - 1. Got failed from payment gateway or payment page closed (Sync Call)
     *                         2. TTL expiry                                             (Async Call)
     * 3. REFUND_INITIATED   - Order cancelled by user will call it via CDC              (Async Call)
     * 4. REFUNDED           - Some job will do it withing SLA                           (Async Call)
     */
    public void updatePayment(Long paymentId, Payment payment) {
        paymentDAO.update(paymentId, payment);
    }

}
