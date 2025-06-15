package com.lld.inventorymanagement.util;

import com.lld.inventorymanagement.util.IOrderObserver;
import com.lld.inventorymanagement.model.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailObserver implements IOrderObserver {

    @Override
    public void onOrderCreated(OrderDTO order) {
        System.out.println("Sent email on order created...");
    }

    @Override
    public void onOrderConfirmed(OrderDTO order) {
        System.out.println("Sent email on order confirmed...");
    }

    @Override
    public void onOrderCanceled(OrderDTO order) {
        System.out.println("Sent email on order canceled...");
    }
}