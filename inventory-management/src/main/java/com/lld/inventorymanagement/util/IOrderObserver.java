package com.lld.inventorymanagement.util;

import com.lld.inventorymanagement.model.dto.OrderDTO;

public interface IOrderObserver {
    void onOrderCreated(OrderDTO order);
    void onOrderConfirmed(OrderDTO order);
    void onOrderCanceled(OrderDTO order);
}