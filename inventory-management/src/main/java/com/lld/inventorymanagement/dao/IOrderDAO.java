package com.lld.inventorymanagement.dao;

import com.lld.inventorymanagement.model.enums.OrderStatus;
import com.lld.inventorymanagement.model.dto.OrderDTO;
import com.lld.inventorymanagement.model.dto.OrderItemDTO;

import java.util.List;

public interface IOrderDAO {
    // Order
    int createOrder(OrderDTO orderDTO);
    void updateOrderStatus(int orderId, OrderStatus status, String reason);
    OrderDTO getOrder(int orderId);
    // OrderItems
    List<OrderItemDTO> createOrderItems(List<OrderItemDTO> orderItemDTOS);
    List<OrderItemDTO> getItemsByOrderId(int orderId);
}