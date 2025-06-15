package com.lld.inventorymanagement.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class OrderCreationRequest {
    private int userId;
    private List<ProductQuantityMapRequest> productQuantity;

    @AllArgsConstructor
    @Builder
    @Data
    @NoArgsConstructor
    public static class ProductQuantityMapRequest {
        private int productId;
        private int quantity;
    }
}
