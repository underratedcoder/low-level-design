package com.lld.inventorymanagement.model.dto;

import com.lld.inventorymanagement.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class OrderDTO {
    private int orderId;
    private int customerId;
    private double totalAmount;
    private OrderStatus status;
    private String reason;
    private Date createdAt;
    private Date updatedAt;
}