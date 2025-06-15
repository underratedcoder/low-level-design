package com.lld.chess.model;

import com.lld.chess.enums.Color;
import com.lld.chess.pieces.Piece;
import lombok.Data;

@Data
public class Cell {
    int row;
    int column;

    Piece piece;

    Color color; // Not of much use

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }
}
