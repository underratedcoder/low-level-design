package com.lld.snakeandladder;

public class Ladder {
    private final int start;
    private final int end;

    public Ladder(int start, int end) {
        if (end <= start) {
            throw new IllegalArgumentException("Ladder must go up.");
        }
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
