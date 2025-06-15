package com.lld.queue.model;

import com.lld.queue.service.Subscriber.ISubscriber;

import java.util.concurrent.atomic.AtomicInteger;

public class TopicSubscriberWithOffset {
    private final Topic topic;
    private final ISubscriber subscriber;
    private final AtomicInteger offset;

    public TopicSubscriberWithOffset(Topic topic, ISubscriber subscriber) {
        this.topic = topic;
        this.subscriber = subscriber;
        this.offset = new AtomicInteger(0);
    }

    public Topic getTopic() {
        return this.topic;
    }

    public ISubscriber getSubscriber() {
        return this.subscriber;
    }

    public AtomicInteger getOffset() {
        return this.offset;
    }
}
