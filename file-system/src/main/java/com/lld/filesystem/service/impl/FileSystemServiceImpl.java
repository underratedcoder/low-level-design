package com.lld.filesystem.service.impl;

import com.lld.filesystem.enums.Permission;
import com.lld.filesystem.exception.*;
import com.lld.filesystem.model.*;
import com.lld.filesystem.repository.*;
import com.lld.filesystem.repository.impl.InMemoryFileSystemRepository;
import com.lld.filesystem.service.FileSystemService;

import java.nio.file.FileAlreadyExistsException;
import java.util.*;

public class FileSystemServiceImpl implements FileSystemService {
    private final FileSystemRepository repository = InMemoryFileSystemRepository.getInstance();

    @Override
    public void createFile(String path, String content, Set<Permission> permissions) throws FileAlreadyExistsException {
        Directory dir = repository.getCurrentDirectory();
        String fileName = path;

        if (dir.getChildren().containsKey(fileName)) {
            throw new FileAlreadyExistsException("File already exists: " + path);
        }

        File file = new File(fileName, content, dir, permissions);
        dir.getChildren().put(fileName, file);
    }

    @Override
    public void createDirectory(String path, Set<Permission> permissions) throws FileAlreadyExistsException {
        Directory dir = repository.getCurrentDirectory();
        String dirName = path;

        if (dir.getChildren().containsKey(dirName)) {
            throw new FileAlreadyExistsException("Directory already exists: " + path);
        }

        Directory newDir = new Directory(dirName, dir, permissions);

        dir.getChildren().put(dirName, newDir);
    }

    @Override
    public String readFile(String path) {
        FileSystemNode node = repository.getNodeByPath(path);

        if (!(node instanceof File)) {
            throw new InvalidOperationException("Not a file: " + path);
        }

        return ((File) node).getContent();
    }

    @Override
    public void updateFile(String path, String newContent) {
        FileSystemNode node = repository.getNodeByPath(path);

        if (!(node instanceof File)) {
            throw new InvalidOperationException("Not a file: " + path);
        }

        File file = (File) node;
        file.setContent(newContent);
        file.setSize(newContent.length());
    }

    @Override
    public void delete(String path) {
        repository.deleteNode(path);
    }

    @Override
    public String pwd() {
        FileSystemNode node = repository.getCurrentDirectory();

        List<String> fullPathList = new ArrayList<>();

        fullPathList.add(node.getName());

        while (node.getParent() != null) {
            node = node.getParent();
            fullPathList.add(node.getName());
        }

        Collections.reverse(fullPathList);

        return String.join("/", fullPathList);
    }

    @Override
    public String ls() {
        StringBuilder sb = new StringBuilder();

        repository
                .getCurrentDirectory()
                .getChildren()
                .forEach((name, node) -> {
                    sb.append(node instanceof Directory ? "[DIR] " : "[FILE] ").append(name).append("\n");
                });

        return sb.toString();
    }

    @Override
    public void cd(String path) {
        Directory currentDirectory = repository.getCurrentDirectory();
        if ("..".equals(path)) {
            if (currentDirectory.getParent() != null) {
                repository.setCurrentDirectory(currentDirectory.getParent());
            }
        } else {
            FileSystemNode node = repository.getNodeByPath(path);
            if (!(node instanceof Directory)) throw new InvalidOperationException("Not a directory: " + path);
            repository.setCurrentDirectory((Directory) node);
        }
    }
}