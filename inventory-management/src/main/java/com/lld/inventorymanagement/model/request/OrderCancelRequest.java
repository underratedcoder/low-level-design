package com.lld.inventorymanagement.model.request;

import com.lld.inventorymanagement.model.enums.OrderCancelReasonCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class OrderCancelRequest {
    private int userId;
    private OrderCancelReasonCode reasonCode;
}
