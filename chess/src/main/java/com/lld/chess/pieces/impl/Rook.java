package com.lld.chess.pieces.impl;

import com.lld.chess.core.Board;
import com.lld.chess.enums.Color;
import com.lld.chess.pieces.Piece;

public class Rook extends Piece {
    public Rook(Color color) {
        super(color);
    }

    @Override
    public boolean canMove(Board board, int row, int col, int destRow, int destCol) {
        return (row == destRow || col == destCol);
    }
}
