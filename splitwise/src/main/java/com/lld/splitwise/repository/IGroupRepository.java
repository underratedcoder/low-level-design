// IGroupRepository.java
package com.lld.splitwise.repository;

import com.lld.splitwise.model.Group;
import java.util.List;
import java.util.UUID;

public interface IGroupRepository {
    Group save(Group group);
    Group findById(UUID id);
    List<Group> findAll();
    void delete(UUID id);
}