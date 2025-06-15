package com.lld.queue.service.Subscriber;

import com.lld.queue.model.Message;
import com.lld.queue.model.Topic;
import com.lld.queue.model.TopicSubscriberWithOffset;

public class SubscriberWorker implements Runnable {
    private final TopicSubscriberWithOffset topicSubscriberWithOffset;

    public SubscriberWorker(TopicSubscriberWithOffset topicSubscriberWithOffset) {
        this.topicSubscriberWithOffset = topicSubscriberWithOffset;
    }

    @Override
    public void run() {
        Topic topic = this.topicSubscriberWithOffset.getTopic();
        ISubscriber subscriber = this.topicSubscriberWithOffset.getSubscriber();

        while (true) {
            Message messageToProcess = null;
            synchronized (this.topicSubscriberWithOffset) {
                // Wait until there is a new message (offset is less than the number of messages)
                while (this.topicSubscriberWithOffset.getOffset().get() >= topic.getMessages().size()) {

                    try {
                        this.topicSubscriberWithOffset.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
                // Retrieve the next message and increment the offset
                int currentOffset = this.topicSubscriberWithOffset.getOffset().getAndIncrement();
                messageToProcess = topic.getMessages().get(currentOffset);
            }
            // Process the message outside of the synchronized block
            try {
                subscriber.consume(messageToProcess);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
