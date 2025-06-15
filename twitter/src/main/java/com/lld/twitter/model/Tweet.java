package com.lld.twitter.model;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tweet {
    private String id;
    private String content;
    private String createdBy;
    private LocalDateTime createdAt;
    private int likeCount;
    private List<Comment> comments;
    private Set<String> hashTags;
}
