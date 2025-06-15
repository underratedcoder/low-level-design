package com.lld.inventorymanagement.model.dto;

import com.lld.inventorymanagement.model.enums.ProductUnitStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ProductUnitDTO {
    private int productUnitId;
    private int productId;
    private int sellerId;
    private double price;
    private ProductUnitStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}