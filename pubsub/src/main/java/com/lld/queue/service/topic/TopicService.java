package com.lld.queue.service.topic;

import com.lld.queue.model.Message;
import com.lld.queue.model.Topic;
import com.lld.queue.model.TopicSubscriberWithOffset;
import com.lld.queue.service.Subscriber.ISubscriber;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service responsible for managing topics - creation, retrieval, and lifecycle management.
 */
public class TopicService {
    private final AtomicInteger topicIdCounter;
    private final Map<String, Topic> topics;
    // Map of topic IDs to their list of TopicSubscriber associations.
    private final Map<String, List<TopicSubscriberWithOffset>> topicSubscribers;

    public TopicService() {
        this.topicIdCounter = new AtomicInteger(0);
        this.topics = new ConcurrentHashMap<>();
        this.topicSubscribers = new ConcurrentHashMap<>();
    }

    /**
     * Creates a new topic with the given name.
     * @param topicName The name of the topic to create
     * @return The created Topic object
     */
    public Topic createTopic(String topicName) {
        String topicId = String.valueOf(topicIdCounter.incrementAndGet());
        Topic topic = new Topic(topicName, topicId);
        topics.put(topicId, topic);
        topicSubscribers.put(topic.getTopicId(), new CopyOnWriteArrayList<>());
        System.out.println("Created topic: " + topicName + " with id: " + topic.getTopicId());
        return topic;
    }

    public void publish(String topicId, Message message) {
        Topic topic = topics.get(topicId);
        if (topic == null) {
            throw new IllegalArgumentException("Topic with id " + topicId + " does not exist");
        }
        // Add message to topic
        topic.addMessage(message);
    }

    public TopicSubscriberWithOffset addSubscriber(String topicId, ISubscriber subscriber) {
        Topic topic = topics.get(topicId);
        if (topic == null) {
            System.err.println("Topic with id " + topicId + " does not exist");
            throw new RuntimeException("");
        }
        TopicSubscriberWithOffset subscriberWithOffset = new TopicSubscriberWithOffset(topic, subscriber);
        List<TopicSubscriberWithOffset> subscribers = topicSubscribers.get(topicId);
        subscribers.add(subscriberWithOffset);
        return subscriberWithOffset;
    }

    public List<TopicSubscriberWithOffset> getSubscribers(String topicId) {
        return topicSubscribers.get(topicId);
    }
}