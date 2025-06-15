package com.lld.airline.dao;

import com.lld.airline.enums.PaymentStatus;
import com.lld.airline.model.Payment;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class PaymentDAO {
    private static volatile PaymentDAO instance;

    public static PaymentDAO getInstance() {
        if (instance == null) {
            synchronized (PaymentDAO.class) {
                if (instance == null) {
                    instance = new PaymentDAO();
                }
            }
        }
        return instance;
    }

    private static final AtomicLong paymentId = new AtomicLong(1);

    private final Map<Long, Payment> paymentMap = new ConcurrentHashMap<>();
    private final Map<Long, Payment> bookingPaymentMap = new ConcurrentHashMap<>();

    /**
     * When user do proceed to pay, This will create entities in the client system
     * and send the request to PaymentGateway in async manner (CDC) with the PaymentId
     * PaymentGateway will generate a payment context against the PaymentId and show a display to users
     * */
    public Payment save(Payment payment) {
        if (!bookingPaymentMap.containsKey(payment.getBookingId())
            || bookingPaymentMap.get(payment.getBookingId()).getStatus() == PaymentStatus.FAILED) {
            Long paymentId = generatePaymentId();

            payment.setPaymentId(paymentId);
            payment.setStatus(PaymentStatus.PAYMENT_INITIATED);

            paymentMap.put(paymentId, payment);
            bookingPaymentMap.put(payment.getBookingId(), payment);

            return payment;
        } else {
            log.warn("Payment already processed for this booking...");
            return bookingPaymentMap.get(payment.getBookingId());
        }
    }

    /**
     * User will make attempt to pay
     * 1. COMPLETED        - User was able to pay
     * 2. FAILED           - User closed the payment page, entered wrong payment detail or some failure at payment gateway side
     * 3. REFUND_INITIATED - User requested REFUND, This will initiate a async event to PaymentGateway
     * 4. REFUNDED         - Users money is REFUNDED
     * */
    public void update(Long paymentId, Payment newPayment) {
        Payment oldPayment = paymentMap.get(paymentId);

        oldPayment.setStatus(newPayment.getStatus());

        if (newPayment.getStatus().equals(PaymentStatus.COMPLETED)) {
            oldPayment.setPaymentMethod(newPayment.getPaymentMethod());
        }
    }

    public Payment get(Long paymentId) {
        return bookingPaymentMap.get(paymentId);
    }

    public Payment getByBookingId(Long bookingId) {
        return bookingPaymentMap.get(bookingId);
    }

    private Long generatePaymentId() {
        return paymentId.getAndIncrement();
    }
}
