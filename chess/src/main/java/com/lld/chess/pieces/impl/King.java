package com.lld.chess.pieces.impl;

import com.lld.chess.core.Board;
import com.lld.chess.enums.Color;
import com.lld.chess.pieces.Piece;

public class King extends Piece {
    public King(Color color) {
        super(color);
    }

    @Override
    public boolean canMove(Board board, int row, int col, int destRow, int destCol) {
        int rowDiff = Math.abs(destRow - row);
        int colDiff = Math.abs(destCol - col);
        return (rowDiff <= 1 && colDiff <= 1);
    }
}
