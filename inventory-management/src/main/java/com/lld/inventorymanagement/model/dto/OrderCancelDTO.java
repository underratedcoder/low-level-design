package com.lld.inventorymanagement.model.dto;

import com.lld.inventorymanagement.model.enums.OrderCancelReasonCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class OrderCancelDTO {
    private int orderId;
    private int userId;
    private OrderCancelReasonCode reasonCode;
}
