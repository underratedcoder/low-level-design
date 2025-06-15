package com.lld.inventorymanagement.service.impl;

import com.lld.inventorymanagement.model.dto.OrderCreationDTO;
import com.lld.inventorymanagement.dao.IInventoryDAO;
import com.lld.inventorymanagement.model.enums.ProductUnitStatus;
import com.lld.inventorymanagement.model.dto.ProductDTO;
import com.lld.inventorymanagement.model.dto.ProductUnitDTO;
import com.lld.inventorymanagement.service.IInventoryService;

import java.util.List;

public class InventoryService implements IInventoryService {

    private final IInventoryDAO inventoryDao;

    public InventoryService(IInventoryDAO inventoryDao) {
        this.inventoryDao = inventoryDao;
    }

    // Product
    public int addProduct(ProductDTO productDTO) {
        return inventoryDao.addProduct(productDTO);
    }

    // ProductUnits
    public void addProductUnits(List<ProductUnitDTO> productUnitDTOS) {
        inventoryDao.addProductUnits(productUnitDTOS);
    }

    public List<ProductUnitDTO> reserveProductUnits(List<OrderCreationDTO.ProductQuantityMapDTO> productQuantityMapDTO) throws Exception {
        return inventoryDao.reserveProductUnits(productQuantityMapDTO);
    }

    public void updateProductUnitStatus(List<Integer> productUnitIds, ProductUnitStatus unitStatus) {
        inventoryDao.updateUnitsStatus(productUnitIds, unitStatus);
    }

    //    public List<ProductUnitDTO> getUnitsByProductId(int productId, int sellerId) {
//        return inventoryDao.getUnitsByProductId(productId, sellerId);
//    }
}