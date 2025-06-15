package com.lld.snake.service;

import com.lld.snake.enums.Direction;
import com.lld.snake.model.Coordinate;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

public class SnakeGame {
    private int moveNumber;

    private final int size;
    private int score;

    private Direction currentDirection;

    private final LinkedList<Coordinate> snake = new LinkedList<>();
    private final Set<Coordinate> unique = new HashSet<>();

    private Coordinate foodPosition;
    private boolean eatFoodInLastStep;
    private boolean gameOver;

    public SnakeGame(int size) {
        this.moveNumber = 0;

        this.size = size;
        this.score = 0;

        this.currentDirection = Direction.RIGHT;

        Coordinate snakeHead = Coordinate.builder().x(0).y(0).build();
        snake.add(snakeHead);
        unique.add(snakeHead);

        generateFood();

        displayBoard();
    }

    public void move(Direction newDirection) {
        if (gameOver) {
            System.out.println("Game Over! Final Score: " + score);
            return;
        }

        if (newDirection != null && moveIsInvalid(newDirection)) {
            System.out.println("Illegal Move");
            return;
        }

        if (newDirection != null) {
            currentDirection = newDirection;
        }

        Coordinate snakeHead = snake.getLast();
        int x = snakeHead.getX(), y = snakeHead.getY();

        switch (currentDirection) {
            case LEFT -> y--;
            case UP -> x--;
            case RIGHT -> y++;
            case DOWN -> x++;
        }

        Coordinate newHead = new Coordinate(x, y);

        if (isSnakeDead(newHead)) {
            gameOver = true;
            System.out.println("Game Over! Final Score: " + score);
            return;
        }

        snake.addLast(newHead);
        unique.add(newHead);

        if (newHead.equals(foodPosition)) {
            score++;
            generateFood();
        } else {
            Coordinate tail = snake.removeFirst();
            unique.remove(tail);
        }

        displayBoard();
    }

    private boolean isSnakeEatFood(Coordinate newHead) {
        return newHead == foodPosition;
    }

    private boolean isSnakeDead(Coordinate head) {
        if (head.getX() < 0 || head.getY() < 0 || head.getX() >= size || head.getY() >= size) {
            return true;
        } else if (unique.contains(head) && head != snake.getFirst()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean moveIsInvalid(Direction newDirection) {
        return (currentDirection == Direction.LEFT && newDirection == Direction.RIGHT) ||
                (currentDirection == Direction.RIGHT && newDirection == Direction.LEFT) ||
                (currentDirection == Direction.UP && newDirection == Direction.DOWN) ||
                (currentDirection == Direction.DOWN && newDirection == Direction.UP);
    }

    private void generateFood() {
        Random random = new Random();
        int maxTries = size * size;

        while (maxTries-- > 0) {
            int fx = random.nextInt(size);
            int fy = random.nextInt(size);
            Coordinate food = Coordinate.builder().x(fx).y(fy).build();
            if (!unique.contains(food)) {
                foodPosition = food;
                return;
            }
        }

        // Edge case: no space left
        foodPosition = null;
        System.out.println("Congratulations! Snake filled the entire grid.");
        gameOver = true;
    }

    public void displayBoard() {
        System.out.println("Current Score: " + score);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Coordinate curr = Coordinate.builder().x(i).y(j).build();
                if (curr.equals(snake.getLast())) {
                    System.out.print("H "); // Head
                } else if (curr.equals(foodPosition)) {
                    System.out.print("F "); // Food
                } else if (unique.contains(curr)) {
                    System.out.print("S "); // Snake body
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

}