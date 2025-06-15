package com.lld.inventorymanagement.service;

import com.lld.inventorymanagement.model.dto.OrderCancelDTO;
import com.lld.inventorymanagement.model.dto.OrderCreationDTO;
import com.lld.inventorymanagement.model.dto.OrderDTO;

public interface IOrderService {
    int createOrder(OrderCreationDTO orderCreationDTO) throws Exception;
    int confirmOrder(int orderId) throws Exception;
    void cancelOrder(OrderCancelDTO orderCancelDTO) throws Exception;
    OrderDTO getOrderById(int orderId) throws Exception;
}
