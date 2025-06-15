// GroupRepository.java
package com.lld.splitwise.repository.impl;

import com.lld.splitwise.model.Group;
import com.lld.splitwise.repository.IGroupRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class GroupRepository implements IGroupRepository {
    private final Map<UUID, Group> groups;

    private GroupRepository() {
        this.groups = new HashMap<>();
    }

    @Override
    public Group save(Group group) {
        if (group.getId() == null) {
            group.setId(UUID.randomUUID());
        }
        groups.put(group.getId(), group);
        return group;
    }

    @Override
    public Group findById(UUID id) {
        return groups.get(id);
    }

    @Override
    public List<Group> findAll() {
        return groups.values().stream().collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        groups.remove(id);
    }

    /**
     * Object Creation
     * */

    private static GroupRepository instance;

    public static synchronized GroupRepository getInstance() {
        if (instance == null) {
            instance = new GroupRepository();
        }
        return instance;
    }
}