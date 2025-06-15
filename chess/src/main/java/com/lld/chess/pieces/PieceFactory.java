package com.lld.chess.pieces;

import com.lld.chess.enums.Color;
import com.lld.chess.enums.PieceType;
import com.lld.chess.pieces.impl.*;

public class PieceFactory {

    public static Piece createPiece(PieceType type, Color color) {
        switch (type) {
            case KING:
                return new King(color);
            case QUEEN:
                return new Queen(color);
            case ROOK:
                return new Rook(color);
            case BISHOP:
                return new Bishop(color);
            case KNIGHT:
                return new Knight(color);
            case PAWN:
                return new Pawn(color);
            default:
                throw new IllegalArgumentException("Invalid piece type: " + type);
        }
    }
}
