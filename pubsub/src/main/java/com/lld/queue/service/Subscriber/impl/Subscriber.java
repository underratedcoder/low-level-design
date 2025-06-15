package com.lld.queue.service.Subscriber.impl;

import com.lld.queue.service.Subscriber.ISubscriber;
import com.lld.queue.model.Message;

public class Subscriber implements ISubscriber {

    private final String id;

    public Subscriber(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void consume(Message message) throws InterruptedException {
        // Processing the received message.
        System.out.println("Subscriber " + id + " received: " + message.getContent());
        // Simulate processing delay if desired
        Thread.sleep(500);
    }
}
