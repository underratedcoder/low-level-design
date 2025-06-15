package com.lld.chess.pieces;

import com.lld.chess.core.Board;
import com.lld.chess.enums.Color;
import lombok.Data;

@Data
public abstract class Piece {
    protected final Color color;
    protected boolean isActive;

    public Piece(Color color) {
        this.color = color;
        this.isActive = true;
    }

    public abstract boolean canMove(Board board, int row, int col, int destRow, int destCol);
}
