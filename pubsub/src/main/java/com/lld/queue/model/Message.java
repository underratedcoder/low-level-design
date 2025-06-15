package com.lld.queue.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String id;
    //private String topicId;
    private String content;
    private LocalDateTime timestamp;
//    private String publisherId;

    public static Message createMessage(String content) {
        return Message.builder()
                .id(UUID.randomUUID().toString())
                .content(content)
                .timestamp(LocalDateTime.now())
                //.publisherId(publisherId)
                .build();
    }
}
