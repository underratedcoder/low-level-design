package com.lld.filesystem.repository.impl;

import com.lld.filesystem.model.*;
import com.lld.filesystem.enums.Permission;
import com.lld.filesystem.repository.FileSystemRepository;

import java.nio.file.FileAlreadyExistsException;
import java.util.*;

public class InMemoryFileSystemRepository implements FileSystemRepository {
    private final Directory root;
    private Directory currentDirectory;

    private InMemoryFileSystemRepository() {
        this.root = new Directory("/", null, EnumSet.allOf(Permission.class));
        this.currentDirectory = root;
    }

    @Override
    public FileSystemNode getNodeByPath(String path) {
        if ("/".equals(path)) {
            return root;
        } else {
            return currentDirectory.getChildren().get(path);
        }
    }

    @Override
    public void saveNode(String path, FileSystemNode node) throws FileAlreadyExistsException {
        if (currentDirectory.getChildren().containsKey(path)) {
            throw new FileAlreadyExistsException("Node already exists at: " + path);
        }
        currentDirectory.getChildren().put(path, node);
        node.setParent(currentDirectory);
    }

    @Override
    public void deleteNode(String path) {
        currentDirectory.getChildren().remove(path);
    }

    @Override
    public Directory getRoot() {
        return root;
    }

    @Override
    public Directory getCurrentDirectory() {
        return currentDirectory;
    }

    @Override
    public void setCurrentDirectory(Directory directory) {
        this.currentDirectory = directory;
    }

    /**
     *  Object Creation
     **/

    private static InMemoryFileSystemRepository instance;

    public static synchronized InMemoryFileSystemRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryFileSystemRepository();
        }
        return instance;
    }
}