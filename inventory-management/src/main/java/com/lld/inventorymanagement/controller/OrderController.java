package com.lld.inventorymanagement.controller;

import com.lld.inventorymanagement.model.dto.OrderCancelDTO;
import com.lld.inventorymanagement.model.dto.OrderCreationDTO;
import com.lld.inventorymanagement.model.dto.OrderDTO;
import com.lld.inventorymanagement.model.request.OrderCancelRequest;
import com.lld.inventorymanagement.model.request.OrderCreationRequest;
import com.lld.inventorymanagement.service.IOrderService;

import java.util.stream.Collectors;

// /api/orders
public class OrderController {
    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    public int createOrder(OrderCreationRequest request) throws Exception {
        OrderCreationDTO orderCreationDTO = OrderCreationDTO.builder()
                .userId(request.getUserId())
                .productQuantity(request.getProductQuantity().stream()
                        .map(pq -> OrderCreationDTO.ProductQuantityMapDTO.builder()
                                .productId(pq.getProductId())
                                .quantity(pq.getQuantity())
                        .build())
                        .collect(Collectors.toList()))
                .build();

        return orderService.createOrder(orderCreationDTO);
    }

    public int confirmOrder(int orderId) throws Exception {
        return orderService.confirmOrder(orderId);
    }

    public void cancelOrder(int orderId, OrderCancelRequest request) throws Exception {
        OrderCancelDTO orderCancelDTO = OrderCancelDTO.builder()
                .orderId(orderId)
                .userId(request.getUserId())
                .reasonCode(request.getReasonCode())
                .build();
        orderService.cancelOrder(orderCancelDTO);
    }

    public OrderDTO getOrderById(int orderId) throws Exception {
        return orderService.getOrderById(orderId);
    }
}