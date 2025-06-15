package com.lld.quickfilesystem.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Directory extends FileSystemNode {

    Map<String, FileSystemNode> children = new HashMap<>();

    public Directory(String name, Directory parent) {
        super(name, LocalDateTime.now(), parent);
    }

    public void addChildren(FileSystemNode fileSystemNode) {
        if (children.containsKey(fileSystemNode.getName())) {
            throw new RuntimeException("File or Directory already exist !!");
        }
        children.put(fileSystemNode.getName(), fileSystemNode);
    }

    public boolean dirExist(String name) {
        if (children.containsKey(name) && children.get(name) instanceof Directory) {
            return true;
        }
        return false;
    }
}
