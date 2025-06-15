You are a seasoned software engineer with 10+ years of experience in writing code in java.
You are giving an interview for a company where you are asked low level design of queue application like apache kafka with below Functional and Non-Functional Requirements 

Functional Requirements -
1. Topics can be created (for ease of design, lets assume we dont have any partition inside topic. topic is the place where message resides)
2. Multiple producers can publish message on a topic
3. Multiple consumers can simultaneously consume messages from a topic. a consumer means a separate entity which will consume messages from topic irrespective of other consumers
4. A consumer can consume messages from more than one topic
5. A offset should be maintained per topic<>consumer pair


Non Functional Requirements -
1. Scalable Entities (Should adapt any new requirements)
2. Handle concurrency (at producer and consumer)
3. At a time, only one producer can produce message to avoid data corruption

Below is the list of entities and services used in the application

Write the code in plain java by following the layered architecture i.e, your code should have different packages such as controller, service, repository, model, enums, exception etc. Create the interfaces whereever needed. Follow SOLID principles and applicable design patterns.

Create packages at this path /Users/akash/Study/Low Level Design/Problems/queue

User lombok framework and do not write boilerplate code