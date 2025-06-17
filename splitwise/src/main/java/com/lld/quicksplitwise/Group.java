package com.lld.quicksplitwise;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Group {
    private final String groupId;
    private final String groupName;

    private final Set<User> members = new HashSet<>();

    public Group(String groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public void addUser(User user) {
        members.add(user);
    }
}
