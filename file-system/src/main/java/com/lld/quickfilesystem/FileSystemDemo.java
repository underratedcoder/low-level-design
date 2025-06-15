package com.lld.quickfilesystem;

import org.interview.service.FileSystemManager;

public class FileSystemDemo {
    public static void main(String[] args) throws InterruptedException {
        FileSystemManager fileSystemManager = new FileSystemManager();
        fileSystemManager.pwd();
        fileSystemManager.ls();

        fileSystemManager.mkDir("level_1_dir_1");
        fileSystemManager.mkDir("level_1_dir_2");

        fileSystemManager.createFile("level_1_file_1", "some content");

        fileSystemManager.ls();

        fileSystemManager.cd("level_1_dir_1");

        fileSystemManager.mkDir("level_2_dir_1");
        fileSystemManager.mkDir("level_2_dir_2");

        fileSystemManager.createFile("level_2_file_1", "some content");

        fileSystemManager.pwd();
        fileSystemManager.ls();

        fileSystemManager.cd("..");

        fileSystemManager.pwd();
        fileSystemManager.ls();



    }
}
