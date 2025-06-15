package com.lld.twitter.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FollowService {
    // userId -> List of people they follow
    private Map<String, List<String>> userFollowing = new HashMap<>();
    // userId -> List of their followers
    private Map<String, List<String>> userFollowers = new HashMap<>();

    public void follow(String followerId, String followeeId) {
        if (followerId.equals(followeeId)) {
            throw new IllegalArgumentException("User cannot follow themselves");
        }

        List<String> following = userFollowing.computeIfAbsent(followerId, k -> new ArrayList<>());
        if (!following.contains(followeeId)) {
            following.add(followeeId);
            userFollowers.computeIfAbsent(followeeId, k -> new ArrayList<>()).add(followerId);
        }
    }

    public void unfollow(String followerId, String followeeId) {
        List<String> following = userFollowing.get(followerId);
        if (following != null) {
            following.remove(followeeId);
        }

        List<String> followers = userFollowers.get(followeeId);
        if (followers != null) {
            followers.remove(followerId);
        }
    }

    public List<String> getFollowing(String userId) {
        return new ArrayList<>(userFollowing.getOrDefault(userId, new ArrayList<>()));
    }

    public List<String> getFollowers(String userId) {
        return new ArrayList<>(userFollowers.getOrDefault(userId, new ArrayList<>()));
    }

    public boolean isFollowing(String followerId, String followeeId) {
        List<String> following = userFollowing.get(followerId);
        return following != null && following.contains(followeeId);
    }
}