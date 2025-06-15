package com.lld.snakeandladder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Board {
    private int size;

    private Map<Integer, Integer> snakes;
    private Map<Integer, Integer> ladders;

    public Board(
            int size,
            List<Snake> snakes,
            List<Ladder> ladders
    ) {
            this.size = size;
            this.snakes = snakes.stream().collect(Collectors.toMap(Snake::getStart, Snake::getEnd));
            this.ladders = ladders.stream().collect(Collectors.toMap(Ladder::getStart, Ladder::getEnd));
    }

    public int getSize() {
        return size;
    }

    public int getNextPosition(int current) {
        if (snakes.containsKey(current)) {
            System.out.println("Snake Bite ...");
            return snakes.get(current);
        }
        if (ladders.containsKey(current)) {
            System.out.println("Ladder Climb ...");
            return ladders.get(current);
        }
        return current;
    }
}
