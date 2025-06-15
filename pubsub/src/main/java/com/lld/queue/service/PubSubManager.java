package com.lld.queue.service;

import com.lld.queue.service.Subscriber.ISubscriber;
import com.lld.queue.model.Message;
import com.lld.queue.model.Topic;
import com.lld.queue.model.TopicSubscriberWithOffset;
import com.lld.queue.service.Subscriber.SubscriberWorker;
import com.lld.queue.service.topic.TopicService;

import java.util.List;
import java.util.concurrent.*;

public class PubSubManager {
    private final TopicService topicService;

    // ExecutorService to run subscriber tasks concurrently.
    private final ExecutorService subscriberExecutor;

    public PubSubManager() {
        this.topicService = new TopicService();
        // Using a cached thread pool to dynamically manage threads.
        this.subscriberExecutor = Executors.newCachedThreadPool();
    }

    public Topic createTopic(String topicName) {
        return topicService.createTopic(topicName);
    }

    public void subscribe(String topicId, ISubscriber subscriber) {

        TopicSubscriberWithOffset subscriberWithOffset =
                topicService.addSubscriber(topicId, subscriber);

        // Submit the subscriber task to the executor.
        // TODO - Do we really need to send the topic or there is any alternate ?
        subscriberExecutor.submit(new SubscriberWorker(subscriberWithOffset));
        System.out.println("Subscriber " + subscriber.getId() + " subscribed to topic: " + subscriberWithOffset.getTopic().getTopicName());
    }

    public void publish(String topicId, Message message) {
        topicService.publish(topicId, message);

        // wake up each subscriber on its own monitor
        List<TopicSubscriberWithOffset> subscribers = topicService.getSubscribers(topicId);
        for (TopicSubscriberWithOffset subscriberWithOffset : subscribers) {
            synchronized (subscriberWithOffset) {
                subscriberWithOffset.notify();
            }
        }

        System.out.println("Message \"" + message.getContent() + "\" published to topicId: " + topicId);
    }

    // Resets the offset for the given subscriber on the specified topic.
//    public void resetOffset(String topicId, ISubscriber subscriber, int newOffset) {
//        List<TopicSubscriberWithOffset> topicSubscriberWithOffsets = topicSubscribers.get(topicId);
//        if (topicSubscriberWithOffsets == null) {
//            System.err.println("Topic with id " + topicId + " does not exist");
//            return;
//        }
//        for (TopicSubscriberWithOffset ts : topicSubscriberWithOffsets) {
//            if (ts.getSubscriber().getId().equals(subscriber.getId())) {
//                ts.getOffset().set(newOffset);
//                // Notify in case the subscriber thread is waiting.
//                synchronized (ts) {
//                    ts.notify();
//                }
//                System.out.println("Offset for subscriber " + subscriber.getId() + " on topic " +
//                        ts.getTopic().getTopicName() + " reset to " + newOffset);
//                break;
//            }
//        }
//    }

    // Shutdown the ExecutorService gracefully.
//    public void shutdown() {
//        subscriberExecutor.shutdown();
//        try {
//            if (!subscriberExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
//                subscriberExecutor.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            subscriberExecutor.shutdownNow();
//        }
//    }
}
