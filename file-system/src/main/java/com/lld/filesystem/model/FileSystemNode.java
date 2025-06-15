package com.lld.filesystem.model;

import com.lld.filesystem.enums.Permission;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class FileSystemNode {
    protected String name;
    protected LocalDateTime creationTime;
    protected Set<Permission> permissions;
    protected Directory parent;

//    protected User owner;
//    protected Set<User> group;
//    protected Set<Permission> ownerPermissions;
//    protected Set<Permission> groupPermissions;
//    protected Set<Permission> othersPermissions;
}