package com.lld.inventorymanagement.controller;

import com.lld.inventorymanagement.model.dto.ProductDTO;
import com.lld.inventorymanagement.model.dto.ProductUnitDTO;
import com.lld.inventorymanagement.service.IInventoryService;
import java.util.List;

public class InventoryController {
    private final IInventoryService inventoryService;

    public InventoryController(IInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public int addProduct(ProductDTO productDTO) {
        return inventoryService.addProduct(productDTO);
    }

    public void addProductUnits(List<ProductUnitDTO> productUnitDTOS) {
        inventoryService.addProductUnits(productUnitDTOS);
    }
}