package com.lld.queue.service.Publisher;
import com.lld.queue.model.Message;

public interface IPublisher {
    String getId();
    void publish(String topicId, Message message) throws IllegalArgumentException;
}
