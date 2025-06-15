package com.lld.chess.pieces.impl;

import com.lld.chess.core.Board;
import com.lld.chess.enums.Color;
import com.lld.chess.pieces.Piece;

public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color);
    }

    @Override
    public boolean canMove(Board board, int row, int col, int destRow, int destCol) {
        int rowDiff = destRow - row;
        int colDiff = Math.abs(destCol - col);

        if (color == Color.WHITE) {
            return (rowDiff == 1 && colDiff == 0) ||
                    (row == 1 && rowDiff == 2 && colDiff == 0) ||
                    (rowDiff == 1 && colDiff == 1 && board.getPiece(destRow, destCol) != null);
        } else {
            return (rowDiff == -1 && colDiff == 0) ||
                    (row == 6 && rowDiff == -2 && colDiff == 0) ||
                    (rowDiff == -1 && colDiff == 1 && board.getPiece(destRow, destCol) != null);
        }
    }
}
