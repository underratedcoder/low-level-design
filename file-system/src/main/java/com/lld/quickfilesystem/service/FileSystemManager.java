package com.lld.quickfilesystem.service;

import org.interview.model.Directory;
import org.interview.model.File;
import org.interview.model.FileSystemNode;

import java.util.ArrayList;
import java.util.List;

public class FileSystemManager {

    private static final String ROOT_DIRECTORY_NAME = "root";

    private final Directory rootDirectory;
    private Directory currDirectory;

    public FileSystemManager() {
        Directory rootDirectory = new Directory(ROOT_DIRECTORY_NAME, null);
        this.rootDirectory = rootDirectory;
        this.currDirectory = rootDirectory;
    }

    public void createFile(String name, String content) {
        File file = new File(name, content, currDirectory);
        currDirectory.addChildren(file);
    }

    public void mkDir(String name) {
        Directory directory = new Directory(name, currDirectory);
        currDirectory.addChildren(directory);
    }

    // directoryName
    // ..
    public void cd(String dir) {
        if (dir.equals("..")) {
            if (currDirectory.equals(rootDirectory)) {
                throw new RuntimeException("At root directory, cant move backward !!");
            }
            this.currDirectory = (Directory) currDirectory.getParent();
        } else {
            this.currDirectory.dirExist(dir);
            this.currDirectory = (Directory) currDirectory.getChildren().get(dir);
        }
    }

    public void pwd() {
        System.out.println("Printing current path ...");
        FileSystemNode temp = currDirectory;
        List<String> path = new ArrayList<>();
        while (temp != null) {
            path.add(temp.getName());
            temp = temp.getParent();
        }

        StringBuilder currPath = new StringBuilder();

        for (int i = path.size() - 1; i >= 0; i--) {
            currPath.append(path.get(i));

            if (i != 0) {
                currPath.append("/");
            }
        }

        System.out.println(currPath);
    }

    public void ls() {
        System.out.println("Printing files/directory at current path ...");
        for (FileSystemNode node : currDirectory.getChildren().values()) {
            System.out.println(node.getName() + " " + node.getClass().getName());
        }
    }
}
