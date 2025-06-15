package com.lld.snake;

import com.lld.snake.enums.Direction;
import com.lld.snake.service.SnakeGame;

import java.util.Scanner;

public class SnakeGameDemo {
    public static void main(String[] args) {
        SnakeGame game = new SnakeGame(6);
        game.displayBoard();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter move (WASD): ");
            String input = scanner.nextLine().toUpperCase();
            switch (input) {
                case "U" -> game.move(Direction.UP);
                case "L" -> game.move(Direction.LEFT);
                case "D" -> game.move(Direction.DOWN);
                case "R" -> game.move(Direction.RIGHT);
                case "" -> game.move(null);
                default -> System.out.println("Invalid input");
            }
        }
    }
}