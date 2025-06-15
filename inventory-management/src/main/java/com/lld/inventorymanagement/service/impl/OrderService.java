package com.lld.inventorymanagement.service.impl;

import com.lld.inventorymanagement.dao.IOrderDAO;
import com.lld.inventorymanagement.service.IInventoryService;
import com.lld.inventorymanagement.util.IOrderObserver;
import com.lld.inventorymanagement.service.IOrderService;
import com.lld.inventorymanagement.model.enums.OrderStatus;
import com.lld.inventorymanagement.model.enums.ProductUnitStatus;
import com.lld.inventorymanagement.model.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService implements IOrderService {
    private final IOrderDAO orderDao;
    private final IInventoryService inventoryService;

    private final List<IOrderObserver> observers;

    public OrderService(IOrderDAO orderDao, IInventoryService inventoryService) {
        this.orderDao = orderDao;
        this.inventoryService = inventoryService;
        this.observers = new ArrayList<>();
    }

    public void addObserver(IOrderObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IOrderObserver observer) {
        observers.remove(observer);
    }

    /**
     * Logic to create an order
     * Reserve product units using InventoryService
     * Calculate total amount
     * Save order and order items to the database
     */
    @Override
    public int createOrder(OrderCreationDTO orderCreationDTO) throws Exception {

        // Reserve product units and mark order as PENDING (This should be one single transaction)
        List<ProductUnitDTO> reservedProductUnits = inventoryService.reserveProductUnits(orderCreationDTO.getProductQuantity());

        // Calculate totalAmount
        double totalAmount = reservedProductUnits.stream()
                .mapToDouble(ProductUnitDTO::getPrice)
                .sum();

        // Create order
        OrderDTO orderDTO = OrderDTO.builder()
                .customerId(orderCreationDTO.getUserId())
                .status(OrderStatus.PENDING)
                .totalAmount(totalAmount)
                .build();
        int orderId = orderDao.createOrder(orderDTO);

        // Create order items
        List<OrderItemDTO> orderItems = reservedProductUnits.stream()
                .map(pu -> OrderItemDTO.builder()
                        .orderId(orderId)
                        .productUnitId(pu.getProductUnitId())
                        .amount(pu.getPrice())
                        .build())
                .collect(Collectors.toList());
        orderDao.createOrderItems(orderItems);

        for (IOrderObserver observer: observers) {
            observer.onOrderCreated(orderDTO);
        }

        return orderId;
    }

    /**
     * Logic to confirm an order - This will be triggered after payment confirmation ( Only system can do this )
     * Confirm reserved product units using InventoryService
     * */
    @Override
    public int confirmOrder(int orderId) throws Exception {
        // Step 1: Fetch the order and its items
        OrderDTO orderDTO = orderDao.getOrder(orderId);
        if (orderDTO == null) {
            throw new Exception("Order not found with ID: " + orderId);
        }

        List<OrderItemDTO> orderItems = orderDao.getItemsByOrderId(orderId);

        List<Integer> productUnitIds = orderItems.stream()
                .map(OrderItemDTO::getProductUnitId)
                .collect(Collectors.toList());

        // Confirm order and mark product units to sold (This should be one single transaction)
        try {
            inventoryService.updateProductUnitStatus(productUnitIds, ProductUnitStatus.SOLD);
            orderDao.updateOrderStatus(orderId, OrderStatus.CONFIRMED, null);
        } catch (Exception ex) {
            throw new Exception("Failed to confirm order: " + ex.getMessage());
        }

        for (IOrderObserver observer: observers) {
            observer.onOrderConfirmed(orderDTO);
        }

        return orderId;
    }

    /**
     * Logic to cancel an order - This can be triggered either by the user or by the system after payment failure
     * Release reserved product units using InventoryService
     * */
    @Override
    public void cancelOrder(OrderCancelDTO orderCancelDTO) throws Exception {
        // Step 1: Fetch the order and its items
        OrderDTO orderDTO = orderDao.getOrder(orderCancelDTO.getOrderId());
        if (orderDTO == null) {
            throw new Exception("Order not found with ID: " + orderCancelDTO.getOrderId());
        }

        List<OrderItemDTO> orderItems = orderDao.getItemsByOrderId(orderCancelDTO.getOrderId());

        List<Integer> productUnitIds = orderItems.stream()
                .map(OrderItemDTO::getProductUnitId)
                .collect(Collectors.toList());

        // Cancel order and mark product units to available (This should be one single transaction)
        try {
            inventoryService.updateProductUnitStatus(productUnitIds, ProductUnitStatus.AVAILABLE);
            orderDao.updateOrderStatus(orderCancelDTO.getOrderId(), OrderStatus.CANCELED, orderCancelDTO.getReasonCode().name());
        } catch (Exception ex) {
            throw new Exception("Failed to cancel order: " + ex.getMessage());
        }

        for (IOrderObserver observer: observers) {
            observer.onOrderCanceled(orderDTO);
        }
    }

    @Override
    public OrderDTO getOrderById(int orderId) throws Exception {
        return orderDao.getOrder(orderId);
    }
}