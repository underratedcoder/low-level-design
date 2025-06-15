package com.lld.filesystem.model;

import com.lld.filesystem.enums.Permission;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class File extends FileSystemNode {
    private String content;
    private long size;

    public File(String name, String content, Directory parent, Set<Permission> permissions) {
        super(name, LocalDateTime.now(), permissions, parent);
        this.content = content;
        this.size = content == null ? 0 : content.length();
    }
}