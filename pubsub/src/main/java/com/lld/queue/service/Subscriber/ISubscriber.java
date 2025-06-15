package com.lld.queue.service.Subscriber;
import com.lld.queue.model.Message;

public interface ISubscriber {
    String getId();
    void consume(Message message) throws InterruptedException;
}
