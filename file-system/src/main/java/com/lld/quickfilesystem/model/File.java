package com.lld.quickfilesystem.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class File extends FileSystemNode {
    private String content;

    public File(String name, String content, Directory parent) {
        super(name, LocalDateTime.now(), parent);
    }
}
