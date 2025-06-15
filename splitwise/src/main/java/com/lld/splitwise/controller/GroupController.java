// GroupController.java
package com.lld.splitwise.controller;

import com.lld.splitwise.model.Group;
import com.lld.splitwise.service.GroupManager;
import java.util.List;
import java.util.UUID;

public class GroupController {
    private final GroupManager groupManager;
    
    public GroupController() {
        this.groupManager = GroupManager.getInstance();
    }
    
    public Group createGroup(String name, String description, UUID creatorId) {
        return groupManager.createGroup(name, description, creatorId);
    }
    
    public Group getGroup(UUID id) {
        return groupManager.getGroup(id);
    }
    
    public List<Group> getAllGroups() {
        return groupManager.getAllGroups();
    }
    
    public Group addMember(UUID groupId, UUID userId) {
        return groupManager.addMember(groupId, userId);
    }
    
    public Group removeMember(UUID groupId, UUID userId) {
        return groupManager.removeMember(groupId, userId);
    }
    
    public void deleteGroup(UUID id) {
        groupManager.deleteGroup(id);
    }
}
