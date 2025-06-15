package com.lld.inventorymanagement.dao.impl;

import com.lld.inventorymanagement.dao.IInventoryDAO;
import com.lld.inventorymanagement.model.enums.ProductUnitStatus;
import com.lld.inventorymanagement.model.dto.OrderCreationDTO;
import com.lld.inventorymanagement.model.dto.ProductDTO;
import com.lld.inventorymanagement.model.dto.ProductUnitDTO;

import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InventoryDAO implements IInventoryDAO {

    private static InventoryDAO instance;

    public static synchronized IInventoryDAO getInstance() {
        if (instance == null) {
            instance = new InventoryDAO();
        }
        return instance;
    }

    private final AtomicInteger productIdGenerator = new AtomicInteger(1);
    private final AtomicInteger productUnitIdGenerator = new AtomicInteger(1);

    // In-memory data structures
    private final Map<Integer, ProductDTO> productTable = new ConcurrentHashMap<>();
    private final Map<Integer, ProductUnitDTO> productUnitTable = new ConcurrentHashMap<>();


    // INSERT INTO product (name, description, category, price) VALUES (?, ?, ?, ?)
    @Override
    public int addProduct(ProductDTO productDTO) {
        int productId = productIdGenerator.getAndIncrement();
        productDTO.setProductId(productId);
        productTable.put(productId, productDTO);
        return productId;
    }

    //  UPDATE product SET name = ?, description = ?, category = ?, price = ? WHERE product_id = ?
    @Override
    public void updateProduct(ProductDTO productDTO) {
        productTable.put(productDTO.getProductId(), productDTO);
    }

    // INSERT INTO product_unit (product_id, seller_id, status, price) VALUES (?, ?, ?, ?)
    @Override
    public void addProductUnits(List<ProductUnitDTO> productUnitDTOS) {
        for (ProductUnitDTO productUnit : productUnitDTOS) {
            int productUnitId = productUnitIdGenerator.getAndIncrement();
            productUnit.setProductUnitId(productUnitId);
            productUnitTable.put(productUnitId, productUnit);
        }
    }
    /**
     *
     * BEGIN TRANSACTION;
     *
     * SELECT * FROM product_unit
     * WHERE product_id = ? AND status = 'AVAILABLE'
     * ORDER BY product_unit_id FOR UPDATE SKIP LOCKED LIMIT ?
     *
     * UPDATE product_unit
     * SET status = 'RESERVED', updated_at = NOW()
     * WHERE product_unit_id IN (789, 790);
     *
     * */
    @Override
    public synchronized List<ProductUnitDTO> reserveProductUnits(List<OrderCreationDTO.ProductQuantityMapDTO> productQuantityMapDTO) {
        List<ProductUnitDTO> reservedUnits = new ArrayList<>();

        for (OrderCreationDTO.ProductQuantityMapDTO pq : productQuantityMapDTO) {
            List<ProductUnitDTO> availableUnits = productUnitTable.values().stream()
                    .filter(pu -> pu.getProductId() == pq.getProductId() && pu.getStatus() == ProductUnitStatus.AVAILABLE)
                    .limit(pq.getQuantity())
                    .collect(Collectors.toList());

            if (availableUnits.size() < pq.getQuantity()) {
                throw new RuntimeException("Not enough inventory available for product ID: " + pq.getProductId());
            }

            reservedUnits.addAll(availableUnits);
        }

        // Update status to RESERVED
        updateUnitsStatus(
                reservedUnits.stream().map(ProductUnitDTO::getProductUnitId).collect(Collectors.toList()),
                ProductUnitStatus.RESERVED
        );

        return reservedUnits;
    }

    // UPDATE product_unit SET status = ? WHERE product_unit_id = ?
    @Override
    public void updateUnitsStatus(List<Integer> productUnitIds, ProductUnitStatus unitStatus) {
        for (int productUnitId : productUnitIds) {
            ProductUnitDTO productUnit = productUnitTable.get(productUnitId);
            if (productUnit != null) {
                productUnit.setStatus(unitStatus);
            }
        }
    }

    public void releaseExpiredReservations() {
        long currentTime = System.currentTimeMillis();

        for (ProductUnitDTO productUnit : productUnitTable.values()) {
            if (productUnit.getStatus() == ProductUnitStatus.RESERVED
                    && currentTime - productUnit.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() > 10 * 60 * 1000
            ) {
                // 10 minutes
                productUnit.setStatus(ProductUnitStatus.AVAILABLE);
            }
        }
    }

    // SELECT * FROM product_unit WHERE product_id = ? AND seller_id = ?
    @Override
    public List<ProductUnitDTO> getUnitsByProductId(int productId, int sellerId) {
        return productUnitTable.values().stream()
                .filter(pu -> pu.getProductId() == productId && pu.getSellerId() == sellerId)
                .collect(Collectors.toList());
    }
}