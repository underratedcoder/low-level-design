package com.lld.filesystem;

import com.lld.filesystem.controller.FileSystemController;
import com.lld.filesystem.service.impl.FileSystemServiceImpl;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileSystemController controller = new FileSystemController(new FileSystemServiceImpl());
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");
            String command = scanner.nextLine();
            controller.handleCommand(command);
        }

    }
}