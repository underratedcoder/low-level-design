package com.lld.inventorymanagement.dao.impl;

import com.lld.inventorymanagement.dao.IOrderDAO;
import com.lld.inventorymanagement.model.enums.OrderStatus;
import com.lld.inventorymanagement.model.dto.OrderDTO;
import com.lld.inventorymanagement.model.dto.OrderItemDTO;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderDAO implements IOrderDAO {
    private final AtomicInteger orderIdGenerator = new AtomicInteger(1);
    private final AtomicInteger orderItemIdGenerator = new AtomicInteger(1);

    // In-memory data structures
    private final Map<Integer, OrderDTO> orderTable = new ConcurrentHashMap<>();;
    private final Map<Integer, List<OrderItemDTO>> orderItemTable = new ConcurrentHashMap<>();;

    // INSERT INTO `order` (customer_id, total_amount, status) VALUES (?, ?, ?)
    @Override
    public int createOrder(OrderDTO orderDTO) {
        int orderId = orderIdGenerator.getAndIncrement();
        orderDTO.setOrderId(orderId);
        orderTable.put(orderId, orderDTO);
        orderItemTable.put(orderId, new ArrayList<>());
        return orderId;
    }

    // UPDATE `order` SET status = ?, reason = ? WHERE order_id = ?
    @Override
    public void updateOrderStatus(int orderId, OrderStatus status, String reason) {
        OrderDTO order = orderTable.get(orderId);
        if (order != null) {
            order.setStatus(status);
        }
    }

    // SELECT * FROM `order` WHERE order_id = ?
    @Override
    public OrderDTO getOrder(int orderId) {
        return orderTable.get(orderId);
    }

    // INSERT INTO order_item (order_id, product_unit_id, amount) VALUES (?, ?, ?)
    @Override
    public List<OrderItemDTO> createOrderItems(List<OrderItemDTO> orderItemDTOS) {
        int orderId = orderItemDTOS.get(0).getOrderId();
        List<OrderItemDTO> items = orderItemTable.get(orderId);

        for (OrderItemDTO item : orderItemDTOS) {
            item.setOrderItemId(orderItemIdGenerator.getAndIncrement());
            items.add(item);
        }

        return items;
    }

    // SELECT * FROM order_item WHERE order_id = ?
    @Override
    public List<OrderItemDTO> getItemsByOrderId(int orderId) {
        return orderItemTable.getOrDefault(orderId, Collections.emptyList());
    }
}