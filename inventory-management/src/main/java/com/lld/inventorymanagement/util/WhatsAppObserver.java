package com.lld.inventorymanagement.util;

import com.lld.inventorymanagement.util.IOrderObserver;
import com.lld.inventorymanagement.model.dto.OrderDTO;

public class WhatsAppObserver implements IOrderObserver {

    @Override
    public void onOrderCreated(OrderDTO order) {
        System.out.println("Sent whatsapp message on order created...");
    }

    @Override
    public void onOrderConfirmed(OrderDTO order) {
        System.out.println("Sent whatsapp message on order confirmed...");
    }

    @Override
    public void onOrderCanceled(OrderDTO order) {
        System.out.println("Sent whatsapp message on order canceled...");
    }
}