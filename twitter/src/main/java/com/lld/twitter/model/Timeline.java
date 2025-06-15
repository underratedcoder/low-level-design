package com.lld.twitter.model;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Timeline {
    private String userId;
    private List<String> tweetIds;
    private LocalDateTime lastUpdatedAt;
}