package com.lld.filesystem.repository;

import com.lld.filesystem.model.Directory;
import com.lld.filesystem.model.FileSystemNode;

import java.nio.file.FileAlreadyExistsException;

public interface FileSystemRepository {
    FileSystemNode getNodeByPath(String path);
    void saveNode(String path, FileSystemNode node) throws FileAlreadyExistsException;
    void deleteNode(String path);
    Directory getRoot();
    Directory getCurrentDirectory();
    void setCurrentDirectory(Directory directory);
}