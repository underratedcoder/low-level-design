package com.lld.chess.core;

import com.lld.chess.enums.Color;
import com.lld.chess.model.Cell;
import com.lld.chess.pieces.Piece;
import com.lld.chess.pieces.PieceFactory;

import static com.lld.chess.enums.PieceType.KNIGHT;
import static com.lld.chess.enums.PieceType.ROOK;
import static com.lld.chess.enums.PieceType.BISHOP;
import static com.lld.chess.enums.PieceType.QUEEN;
import static com.lld.chess.enums.PieceType.KING;
import static com.lld.chess.enums.PieceType.PAWN;

public class Board {
    private final Cell[][] cells;

    public Board() {
        this.cells = new Cell[8][8];
        initializeBoard();
    }

    private void initializeBoard() {

        // Initialise empty boxes in the board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.cells[i][j] = new Cell(i, j);
            }
        }
    }

    public void putPiecesOnBoard() {
        // Put White Pieces
        cells[0][0].setPiece(PieceFactory.createPiece(ROOK, Color.WHITE));
        cells[0][1].setPiece(PieceFactory.createPiece(KNIGHT, Color.WHITE));
        cells[0][2].setPiece(PieceFactory.createPiece(BISHOP, Color.WHITE));
        cells[0][3].setPiece(PieceFactory.createPiece(QUEEN, Color.WHITE));
        cells[0][4].setPiece(PieceFactory.createPiece(KING, Color.WHITE));
        cells[0][5].setPiece(PieceFactory.createPiece(BISHOP, Color.WHITE));
        cells[0][6].setPiece(PieceFactory.createPiece(KNIGHT, Color.WHITE));
        cells[0][7].setPiece(PieceFactory.createPiece(ROOK, Color.WHITE));

        for (int i = 0; i < 8; i++) {
            cells[1][i].setPiece(PieceFactory.createPiece(PAWN, Color.WHITE));
        }

        // Put Black Pieces
        cells[7][0].setPiece(PieceFactory.createPiece(ROOK, Color.BLACK));
        cells[7][1].setPiece(PieceFactory.createPiece(KNIGHT, Color.BLACK));
        cells[7][2].setPiece(PieceFactory.createPiece(BISHOP, Color.BLACK));
        cells[7][3].setPiece(PieceFactory.createPiece(QUEEN, Color.BLACK));
        cells[7][4].setPiece(PieceFactory.createPiece(KING, Color.BLACK));
        cells[7][5].setPiece(PieceFactory.createPiece(BISHOP, Color.BLACK));
        cells[7][6].setPiece(PieceFactory.createPiece(KNIGHT, Color.BLACK));
        cells[7][7].setPiece(PieceFactory.createPiece(ROOK, Color.BLACK));

        for (int i = 0; i < 8; i++) {
            cells[6][i].setPiece(PieceFactory.createPiece(PAWN, Color.BLACK));
        }
    }

    public Piece getPiece(int row, int col) {
        return cells[row][col].getPiece();
    }

    public void putPiece(int row, int col, Piece piece) {
        cells[row][col].setPiece(piece);
    }

    public void removePiece(int row, int col) {
        cells[row][col].setPiece(null);
    }

}
