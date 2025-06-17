package com.lld.parking.quick.model;

import com.lld.parking.quick.enums.PaymentMethod;
import com.lld.parking.quick.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReceipt {
    private String ticketId;
    private double amount;
    private PaymentMethod method;
    private LocalDateTime paidAt;
    private PaymentStatus status;
}