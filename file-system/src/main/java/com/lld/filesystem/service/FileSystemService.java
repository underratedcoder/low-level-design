package com.lld.filesystem.service;

import com.lld.filesystem.enums.Permission;

import java.nio.file.FileAlreadyExistsException;
import java.util.Set;

public interface FileSystemService {
    void createFile(String path, String content, Set<Permission> permissions) throws FileAlreadyExistsException;
    void createDirectory(String path, Set<Permission> permissions) throws FileAlreadyExistsException;
    String readFile(String path);
    void updateFile(String path, String newContent);
    void delete(String path);
    String pwd();
    String ls();
    void cd(String path);
}