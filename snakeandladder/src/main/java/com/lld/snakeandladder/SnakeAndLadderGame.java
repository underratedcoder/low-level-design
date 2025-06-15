package com.lld.snakeandladder;

import java.util.Arrays;
import java.util.List;

public class SnakeAndLadderGame {
    public static void main(String[] args) throws InterruptedException {
        int size = 100;

        List<Snake> snakes = List.of(
                new Snake(90, 54),
                new Snake(70, 55),
                new Snake(52, 42)
        );

        List<Ladder> ladders = List.of(
                new Ladder(3, 22),
                new Ladder(5, 8),
                new Ladder(11, 26)
        );

        Board board = new Board(
                size,
                snakes,
                ladders
        );

        Dice dice = new Dice();

        Game game = new Game(board, dice);
        game.initialise(
                Arrays.asList(
                        new Player("Alice"),
                        new Player("Bob")
                )
        );

        game.play();
    }
}