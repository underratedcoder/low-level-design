package com.lld.inventorymanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class OrderItemDTO {
    private int orderItemId;
    private int orderId;
    private int productUnitId;
    private double amount;
}