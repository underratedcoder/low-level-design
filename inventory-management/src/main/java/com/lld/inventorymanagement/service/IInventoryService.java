package com.lld.inventorymanagement.service;

import com.lld.inventorymanagement.model.dto.OrderCreationDTO;
import com.lld.inventorymanagement.model.enums.ProductUnitStatus;
import com.lld.inventorymanagement.model.dto.ProductDTO;
import com.lld.inventorymanagement.model.dto.ProductUnitDTO;

import java.util.List;

public interface IInventoryService {
    // Product
    int addProduct(ProductDTO productDTO);

    // ProductUnits
    void addProductUnits(List<ProductUnitDTO> productUnitDTOS);
    List<ProductUnitDTO> reserveProductUnits(List<OrderCreationDTO.ProductQuantityMapDTO> productQuantityMapDTO) throws Exception;
    void updateProductUnitStatus(List<Integer> productUnitIds, ProductUnitStatus unitStatus);
//    List<ProductUnitDTO> getUnitsByProductId(int productId, int sellerId);
}
