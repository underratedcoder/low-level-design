package com.lld.airline.model;

import com.lld.airline.enums.PaymentMethodType;
import com.lld.airline.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Payment {
    private Long paymentId;
    private Long bookingId;
    private PaymentMethodType paymentMethod;
    private double amount;
    private PaymentStatus status;
    private LocalDateTime createdDatetime;
    private LocalDateTime updatedDatetime;
}