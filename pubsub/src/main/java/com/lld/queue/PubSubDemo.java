package com.lld.queue;

import com.lld.queue.model.Message;
import com.lld.queue.model.Topic;
import com.lld.queue.service.PubSubManager;
import com.lld.queue.service.Publisher.IPublisher;
import com.lld.queue.service.Publisher.impl.Publisher;
import com.lld.queue.service.Subscriber.ISubscriber;
import com.lld.queue.service.Subscriber.impl.Subscriber;

public class PubSubDemo {
    public static void main(String[] args) throws InterruptedException {
        PubSubManager pubSubManager = new PubSubManager();

        // Create topics.
        Topic topic1 = pubSubManager.createTopic("Topic1");
        Topic topic2 = pubSubManager.createTopic("Topic2");

        // Create subscribers.
        ISubscriber subscriber1 = new Subscriber("Subscriber1");
        ISubscriber subscriber2 = new Subscriber("Subscriber2");
        ISubscriber subscriber3 = new Subscriber("Subscriber3");

        // Subscribe: subscriber1 subscribes to both topics,
        // subscriber2 subscribes to topic1, and subscriber3 subscribes to topic2.
        pubSubManager.subscribe(topic1.getTopicId(), subscriber1);
        pubSubManager.subscribe(topic2.getTopicId(), subscriber2);
        pubSubManager.subscribe(topic1.getTopicId(), subscriber3);
        pubSubManager.subscribe(topic2.getTopicId(), subscriber3);

        IPublisher publisher1 = new Publisher("Publisher1", pubSubManager);
        IPublisher publisher2 = new Publisher("Publisher2", pubSubManager);

        System.out.println("");
        // Publish some messages.
        publisher1.publish(topic1.getTopicId(), Message.createMessage("Message m1"));
        Thread.sleep(1000);
        System.out.println("");

        publisher1.publish(topic1.getTopicId(), Message.createMessage("Message m2"));
        Thread.sleep(1000);
        System.out.println("");

        publisher2.publish(topic2.getTopicId(), Message.createMessage("Message m3"));
        Thread.sleep(1000);
        System.out.println("");

        publisher2.publish(topic2.getTopicId(), Message.createMessage("Message m4"));
        Thread.sleep(1000);
        System.out.println("");

        publisher1.publish(topic1.getTopicId(), Message.createMessage("Message m5"));
        Thread.sleep(1000);
        System.out.println("");
//        // Reset offset for subscriber1 on topic1 (for example, to re-process messages).
//        kafkaController.resetOffset(topic1.getTopicId(), subscriber1, 0);
//
//        // Allow some time before shutting down.
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        kafkaController.shutdown();
    }
}


/*
Output :

Created topic: Topic1 with id: 1
Created topic: Topic2 with id: 2
Subscriber Subscriber1 subscribed to topic: Topic1
Subscriber Subscriber1 subscribed to topic: Topic2
Subscriber Subscriber2 subscribed to topic: Topic1
Subscriber Subscriber3 subscribed to topic: Topic2
Subscriber Subscriber2 received: Message m1
Subscriber Subscriber1 received: Message m1
Message "Message m1" published to topic: Topic1
Publisher Publisher1 published: Message m1 to topic 1
Message "Message m2" published to topic: Topic1
Publisher Publisher1 published: Message m2 to topic 1
Message "Message m3" published to topic: Topic2
Publisher Publisher2 published: Message m3 to topic 2
Subscriber Subscriber1 received: Message m3
Subscriber Subscriber3 received: Message m3
Subscriber Subscriber1 received: Message m2
Subscriber Subscriber2 received: Message m2
Message "Message m4" published to topic: Topic2
Subscriber Subscriber1 received: Message m4
Subscriber Subscriber3 received: Message m4
Publisher Publisher2 published: Message m4 to topic 2
Message "Message m5" published to topic: Topic1
Publisher Publisher1 published: Message m5 to topic 1
Subscriber Subscriber2 received: Message m5
Subscriber Subscriber1 received: Message m5
Offset for subscriber Subscriber1 on topic Topic1 reset to 0
Subscriber Subscriber1 received: Message m1
Subscriber Subscriber1 received: Message m2
Subscriber Subscriber1 received: Message m5

 */