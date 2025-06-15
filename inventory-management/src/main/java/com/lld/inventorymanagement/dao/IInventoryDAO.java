package com.lld.inventorymanagement.dao;

import com.lld.inventorymanagement.model.dto.OrderCreationDTO;
import com.lld.inventorymanagement.model.dto.ProductDTO;
import com.lld.inventorymanagement.model.dto.ProductUnitDTO;
import com.lld.inventorymanagement.model.enums.ProductUnitStatus;

import java.util.List;

public interface IInventoryDAO {
    // Product
    int addProduct(ProductDTO productDTO);
    void updateProduct(ProductDTO productDTO); // Update Product Details ex - price, description etc

    // ProductUnits
    void addProductUnits(List<ProductUnitDTO> productUnitDTOS);
    List<ProductUnitDTO> reserveProductUnits(List<OrderCreationDTO.ProductQuantityMapDTO> productQuantityMapDTO);
    void updateUnitsStatus(List<Integer> productUnitIds, ProductUnitStatus unitStatus);
    void releaseExpiredReservations();
    List<ProductUnitDTO> getUnitsByProductId(int productId, int sellerId);
}