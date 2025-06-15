package com.lld.twitter;

import com.lld.twitter.model.Tweet;
import com.lld.twitter.model.User;
import com.lld.twitter.service.FollowService;
import com.lld.twitter.service.TimelineService;
import com.lld.twitter.service.TweetService;
import com.lld.twitter.service.UserService;

import java.util.List;

public class TwitterSystem {
    private UserService userService;
    private FollowService followService;
    private TweetService tweetService;
    private TimelineService timelineService;

    public TwitterSystem() {
        this.userService = new UserService();
        this.followService = new FollowService();
        this.tweetService = new TweetService();
        this.timelineService = new TimelineService(tweetService, followService);

        // Register TimelineService as observer for TweetService
        this.tweetService.addObserver(timelineService);
    }

    // User operations
    public User createUser(String name, String email) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        return userService.create(name.trim(), email.trim());
    }

    public User getUser(String userId) {
        return userService.fetch(userId);
    }

    public void followUser(String followerId, String followeeId) {
        if (!userService.exists(followerId)) {
            throw new IllegalArgumentException("Follower user does not exist");
        }
        if (!userService.exists(followeeId)) {
            throw new IllegalArgumentException("Followee user does not exist");
        }
        followService.follow(followerId, followeeId);
    }

    public void unfollowUser(String followerId, String followeeId) {
        followService.unfollow(followerId, followeeId);
    }

    public List<String> getFollowing(String userId) {
        return followService.getFollowing(userId);
    }

    public List<String> getFollowers(String userId) {
        return followService.getFollowers(userId);
    }

    public boolean isFollowing(String followerId, String followeeId) {
        return followService.isFollowing(followerId, followeeId);
    }

    // Tweet operations
    public Tweet createTweet(String userId, String content) {
        if (!userService.exists(userId)) {
            throw new IllegalArgumentException("User does not exist");
        }
        if (content == null || content.trim().length() > 280) {
            throw new IllegalArgumentException("Tweet must be between 1 and 280 characters");
        }
        return tweetService.create(userId, content);
    }

    public Tweet getTweet(String tweetId) {
        return tweetService.fetch(tweetId);
    }

    public void likeTweet(String tweetId, String userId) {
        if (!userService.exists(userId)) {
            throw new IllegalArgumentException("User does not exist");
        }
        tweetService.like(tweetId, userId);
    }

    public void unlikeTweet(String tweetId, String userId) {
        if (!userService.exists(userId)) {
            throw new IllegalArgumentException("User does not exist");
        }
        tweetService.unlike(tweetId, userId);
    }

    public boolean hasUserLikedTweet(String tweetId, String userId) {
        return tweetService.hasLiked(tweetId, userId);
    }

    public void commentOnTweet(String tweetId, String userId, String content) {
        if (!userService.exists(userId)) {
            throw new IllegalArgumentException("User does not exist");
        }
        tweetService.comment(tweetId, userId, content);
    }

    public void deleteTweet(String tweetId, String userId) {
        if (!userService.exists(userId)) {
            throw new IllegalArgumentException("User does not exist");
        }
        tweetService.deleteTweet(tweetId, userId);
    }

    public List<Tweet> getUserTweets(String userId) {
        return tweetService.fetchByUser(userId);
    }

    // Timeline operations
    public List<Tweet> getUserTimeline(String userId) {
        if (!userService.exists(userId)) {
            throw new IllegalArgumentException("User does not exist");
        }
        return timelineService.fetchTimeline(userId);
    }

    // Search operations
    public List<Tweet> searchTweetsByHashtag(String hashtag) {
        return tweetService.searchByHashtag(hashtag);
    }
}