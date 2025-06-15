package com.lld.filesystem.model;

import com.lld.filesystem.enums.Permission;
import lombok.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@NoArgsConstructor
public class Directory extends FileSystemNode {
    private Map<String, FileSystemNode> children = new ConcurrentHashMap<>();

    public Directory(String name, Directory parent, Set<Permission> permissions) {
        super(name, java.time.LocalDateTime.now(), permissions, parent);
    }
}