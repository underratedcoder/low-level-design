// GroupManager.java
package com.lld.splitwise.service;

import com.lld.splitwise.exception.GroupNotFoundException;
import com.lld.splitwise.exception.UserNotFoundException;
import com.lld.splitwise.model.Group;
import com.lld.splitwise.model.Member;
import com.lld.splitwise.repository.impl.GroupRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroupManager {
    private final GroupRepository groupRepository;
    private final UserService userService;
    
    private GroupManager() {
        groupRepository = GroupRepository.getInstance();
        userService = UserService.getInstance();
    }
    
    public Group createGroup(String name, String description, UUID creatorId) {
        Member creator = userService.getUser(creatorId);
        List<Member> members = new ArrayList<>();
        members.add(creator);
        
        Group group = Group.builder()
                .name(name)
                .description(description)
                .members(members)
                .build();
        
        return groupRepository.save(group);
    }
    
    public Group getGroup(UUID id) {
        Group group = groupRepository.findById(id);
        if (group == null) {
            throw new GroupNotFoundException("Group not found with id: " + id);
        }
        return group;
    }
    
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }
    
    public Group addMember(UUID groupId, UUID userId) {
        Group group = getGroup(groupId);
        Member member = userService.getUser(userId);
        
        // Check if user already in the group
        boolean isMember = group.getMembers().stream()
                .anyMatch(m -> m.getId().equals(userId));
        
        if (!isMember) {
            group.getMembers().add(member);
            groupRepository.save(group);
        }
        
        return group;
    }
    
    public Group removeMember(UUID groupId, UUID userId) {
        Group group = getGroup(groupId);
        
        boolean removed = group.getMembers().removeIf(member -> member.getId().equals(userId));
        
        if (!removed) {
            throw new UserNotFoundException("User not found in group");
        }
        
        return groupRepository.save(group);
    }
    
    public void deleteGroup(UUID id) {
        groupRepository.delete(id);
    }

    /**
     * Object Creation
     */

    private static GroupManager instance;

    public static synchronized GroupManager getInstance() {
        if (instance == null) {
            instance = new GroupManager();
        }
        return instance;
    }
}