package com.lld.twitter.service;

import com.lld.twitter.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserService {
    private Map<String, User> users = new HashMap<>();

    public User create(String name, String email) {
        String id = UUID.randomUUID().toString();
        User user = User.builder()
                .id(id)
                .name(name)
                .email(email)
                .build();
        users.put(id, user);
        return user;
    }

    public User fetch(String userId) {
        return users.get(userId);
    }

    public boolean exists(String userId) {
        return users.containsKey(userId);
    }
}
