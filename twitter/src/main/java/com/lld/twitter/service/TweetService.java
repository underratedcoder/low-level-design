package com.lld.twitter.service;

import com.lld.twitter.model.Comment;
import com.lld.twitter.model.Tweet;
import com.lld.twitter.observer.TweetSubject;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TweetService extends TweetSubject {
    private Map<String, Tweet> tweets = new HashMap<>();
    private Map<String, Set<String>> hashtagIndex = new HashMap<>(); // hashtag -> tweetIds
    private Map<String, Set<String>> userLikes = new HashMap<>(); // userId -> tweetIds they liked

    public Tweet create(String userId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Tweet content cannot be empty");
        }

        String id = UUID.randomUUID().toString();
        Tweet tweet = Tweet.builder()
                .id(id)
                .content(content.trim())
                .createdBy(userId)
                .createdAt(LocalDateTime.now())
                .likeCount(0)
                .comments(new ArrayList<>())
                .build();

        tweets.put(id, tweet);
        indexHashtags(id, content);

        // Notify observers (Timeline service) about new tweet
        notifyObservers(userId, tweet);

        return tweet;
    }

    public Tweet fetch(String tweetId) {
        return tweets.get(tweetId);
    }

    public void like(String tweetId, String userId) {
        Tweet tweet = tweets.get(tweetId);
        if (tweet != null) {
            Set<String> likedTweets = userLikes.computeIfAbsent(userId, k -> new HashSet<>());
            if (!likedTweets.contains(tweetId)) {
                likedTweets.add(tweetId);
                tweet.setLikeCount(tweet.getLikeCount() + 1);
            }
        }
    }

    public void unlike(String tweetId, String userId) {
        Tweet tweet = tweets.get(tweetId);
        if (tweet != null) {
            Set<String> likedTweets = userLikes.get(userId);
            if (likedTweets != null && likedTweets.contains(tweetId)) {
                likedTweets.remove(tweetId);
                tweet.setLikeCount(Math.max(0, tweet.getLikeCount() - 1));
            }
        }
    }

    public boolean hasLiked(String tweetId, String userId) {
        Set<String> likedTweets = userLikes.get(userId);
        return likedTweets != null && likedTweets.contains(tweetId);
    }

    public void comment(String tweetId, String userId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }

        Tweet tweet = tweets.get(tweetId);
        if (tweet != null) {
            Comment comment = Comment.builder()
                    .id(UUID.randomUUID().toString())
                    .content(content.trim())
                    .createdBy(userId)
                    .createdAt(LocalDateTime.now())
                    .tweetId(tweetId)
                    .build();
            tweet.getComments().add(comment);
        }
    }

    public List<Tweet> fetchByUser(String userId) {
        return tweets.values().stream()
                .filter(tweet -> tweet.getCreatedBy().equals(userId))
                .sorted((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public List<Tweet> searchByHashtag(String hashtag) {
        if (hashtag == null || hashtag.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String cleanHashtag = hashtag.startsWith("#") ? hashtag.substring(1).toLowerCase() : hashtag.toLowerCase();
        Set<String> tweetIds = hashtagIndex.getOrDefault(cleanHashtag, new HashSet<>());
        return tweetIds.stream()
                .map(tweets::get)
                .filter(Objects::nonNull)
                .sorted((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public void deleteTweet(String tweetId, String userId) {
        Tweet tweet = tweets.get(tweetId);
        if (tweet != null && tweet.getCreatedBy().equals(userId)) {
            tweets.remove(tweetId);
            removeFromHashtagIndex(tweetId, tweet.getContent());
            // Remove from user likes
            userLikes.values().forEach(likedTweets -> likedTweets.remove(tweetId));
        }
    }

    private void indexHashtags(String tweetId, String content) {
        String[] words = content.split("\\s+");
        for (String word : words) {
            if (word.startsWith("#") && word.length() > 1) {
                String hashtag = word.substring(1).toLowerCase();
                hashtagIndex.computeIfAbsent(hashtag, k -> new HashSet<>()).add(tweetId);
            }
        }
    }

    private void removeFromHashtagIndex(String tweetId, String content) {
        String[] words = content.split("\\s+");
        for (String word : words) {
            if (word.startsWith("#") && word.length() > 1) {
                String hashtag = word.substring(1).toLowerCase();
                Set<String> tweetIds = hashtagIndex.get(hashtag);
                if (tweetIds != null) {
                    tweetIds.remove(tweetId);
                    if (tweetIds.isEmpty()) {
                        hashtagIndex.remove(hashtag);
                    }
                }
            }
        }
    }
}