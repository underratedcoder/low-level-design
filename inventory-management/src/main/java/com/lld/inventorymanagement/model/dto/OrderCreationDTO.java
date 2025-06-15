package com.lld.inventorymanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class OrderCreationDTO {
    private int userId;
    private List<ProductQuantityMapDTO> productQuantity;

    @AllArgsConstructor
    @Builder
    @Data
    @NoArgsConstructor
    public static class ProductQuantityMapDTO {
        private int productId;
        private int quantity;
    }
}
