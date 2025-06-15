package com.lld.chess.core;

import com.lld.chess.enums.Color;
import com.lld.chess.model.Move;
import com.lld.chess.pieces.Piece;

public class Player {
    private final String name;
    private final Color color;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
