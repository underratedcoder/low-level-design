package com.lld.queue.service.Publisher.impl;


import com.lld.queue.service.Publisher.IPublisher;
import com.lld.queue.service.PubSubManager;
import com.lld.queue.model.Message;

public class Publisher implements IPublisher {
    private final String id;
    private final PubSubManager pubSubManager;

    public Publisher(String id, PubSubManager pubSubManager) {
        this.id = id;
        this.pubSubManager = pubSubManager;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void publish(String topicId, Message message) throws IllegalArgumentException {
        System.out.println("Publisher " + id + " published: " + message.getContent() + " to topic " + topicId);
        pubSubManager.publish(topicId, message);
    }
}
