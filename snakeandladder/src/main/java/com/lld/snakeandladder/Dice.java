package com.lld.snakeandladder;

import java.util.Random;

public class Dice {
    private static final int MAX = 6;
    private Random random;

    public Dice() {
        random = new Random();
    }

    public int roll() {
        return random.nextInt(MAX) + 1; // 1 to 6
    }
}
