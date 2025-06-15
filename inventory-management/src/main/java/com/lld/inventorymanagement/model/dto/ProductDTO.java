package com.lld.inventorymanagement.model.dto;

import com.lld.inventorymanagement.model.enums.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ProductDTO {
    private int productId;
    private String name;
    private String description;
    private ProductCategory category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}