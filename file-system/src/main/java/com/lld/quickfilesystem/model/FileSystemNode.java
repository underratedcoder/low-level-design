package com.lld.quickfilesystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Getter
public class FileSystemNode {
    protected String name;
    protected LocalDateTime createdAt;
    protected FileSystemNode parent;
}
