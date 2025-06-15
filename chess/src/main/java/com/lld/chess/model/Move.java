package com.lld.chess.model;

import com.lld.chess.pieces.Piece;
import lombok.Getter;

@Getter
public class Move {
    private final Piece piece;

    private final int srcRow;
    private final int srcCol;

    private final int destRow;
    private final int destCol;

    public Move(Piece piece, int srcRow, int srcCol, int destRow, int destCol) {
        this.piece = piece;
        this.srcRow = srcRow;
        this.srcCol = srcCol;
        this.destRow = destRow;
        this.destCol = destCol;
    }
}
