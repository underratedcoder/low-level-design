package com.lld.snakeandladder;

import java.util.*;

public class Game {
    private final Board board;
    private final Dice dice;
    private Queue<Player> turnQueue;
    private Map<Player, Integer> playerPositions;

    public Game(Board board, Dice dice) {
        this.board = board;
        this.dice = dice;
    }

    public void initialise(List<Player> players) {
        this.turnQueue = new LinkedList<>(players);
        this.playerPositions = new HashMap<>();

        for (Player player : players) {
            playerPositions.put(player, 0); // All start at position 0
        }
    }

    public void play() throws InterruptedException {
        while (true) {
            Player currentPlayer = turnQueue.poll();
            int currentPosition = playerPositions.get(currentPlayer);

            int roll = dice.roll();
            System.out.println(currentPlayer.getName() + " rolled: " + roll);

            Thread.sleep(1000);

            int tentativePosition = currentPosition + roll;
            if (tentativePosition > board.getSize()) {
                System.out.println(currentPlayer.getName() + " rolled too high, stays at: " + currentPosition);
                turnQueue.offer(currentPlayer);
                continue;
            }

            int finalPosition = board.getNextPosition(tentativePosition);
            playerPositions.put(currentPlayer, finalPosition);
            System.out.println(currentPlayer.getName() + " moved to: " + finalPosition);

            if (finalPosition == board.getSize()) {
                System.out.println(currentPlayer.getName() + " wins!");
                break;
            }

            turnQueue.offer(currentPlayer);
        }
    }
}
