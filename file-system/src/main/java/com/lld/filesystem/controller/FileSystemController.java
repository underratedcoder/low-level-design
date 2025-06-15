package com.lld.filesystem.controller;

import com.lld.filesystem.enums.Permission;
import com.lld.filesystem.service.FileSystemService;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Set;

@RequiredArgsConstructor
public class FileSystemController {
    private final FileSystemService fileSystemService;

    public void handleCommand(String commandLine) {
        try {

            String[] tokens = commandLine.trim().split("\\s+");

            switch (tokens[0]) {
                case "pwd":
                    System.out.println(fileSystemService.pwd());
                    break;
                case "ls":
                    System.out.print(fileSystemService.ls());
                    break;
                case "cd":
                    fileSystemService.cd(tokens[1]);
                    break;
                case "touch":
                    fileSystemService.createFile(tokens[1], tokens[2], EnumSet.allOf(Permission.class));
                    break;
                case "mkdir":
                    fileSystemService.createDirectory(tokens[1], EnumSet.allOf(Permission.class));
                    break;
                case "cat":
                    System.out.println(fileSystemService.readFile(tokens[1]));
                    break;
                case "rm":
                    fileSystemService.delete(tokens[1]);
                    break;
                default:
                    System.out.println("Unknown command");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}