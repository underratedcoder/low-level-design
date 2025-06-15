package com.lld.chess.core;

import com.lld.chess.enums.Color;
import com.lld.chess.model.Move;
import com.lld.chess.pieces.Piece;
import com.lld.chess.pieces.impl.King;
import com.lld.chess.pieces.impl.Knight;

public class MoveValidator {
    private final Board board;
    
    public MoveValidator(Board board) {
        this.board = board;
    }
    
    public boolean isValidMove(Move move) {

        int srcRow = move.getSrcRow();
        int srcCol = move.getSrcCol();
        int destRow = move.getDestRow();
        int destCol = move.getDestCol();

        Piece piece = move.getPiece();
        
        // Basic validation
        if (srcRow < 0 || srcRow > 7 || srcCol < 0 || srcCol > 7 ||
            destRow < 0 || destRow > 7 || destCol < 0 || destCol > 7) {
            return false;
        }
        
        // Cannot move to the same position
        if (srcRow == destRow && srcCol == destCol) {
            return false;
        }
        
        // Validate piece movement pattern
        if (!piece.canMove(board, srcRow, srcCol, destRow, destCol)) {
            return false;
        }
        
        // Check if path is clear (for pieces that require this)
        if (!isPathClear(srcRow, srcCol, destRow, destCol)) {
            return false;
        }
        
        // Check if the destination has a piece of the same color
        Piece destPiece = board.getPiece(destRow, destCol);
        if (destPiece != null && destPiece.getColor() == piece.getColor()) {
            return false;
        }
        
        // Check if the move leaves the king in check
        if (leavesKingInCheck(move, piece.getColor())) {
            return false;
        }

        return true;
    }

    public boolean isGameOver() {
        return isCheckmate(Color.WHITE)
                || isCheckmate(Color.BLACK)
                || isStalemate(Color.WHITE)
                || isStalemate(Color.BLACK);
    }

    private boolean isCheckmate(Color color) {
        // First, check if the king is in check
        if (!isKingInCheck(color)) {
            return false; // Can't be checkmate if king is not in check
        }

        // If king is in check, try all possible moves for all pieces of that color
        return !hasLegalMoves(color);
    }

    private boolean isStalemate(Color color) {
        // In stalemate, king is NOT in check
        if (isKingInCheck(color)) {
            return false; // Can't be stalemate if king is in check
        }

        // But the player has no legal moves
        return !hasLegalMoves(color);
    }

    /**
     * Determines if a king of specified color is currently in check
     */
    private boolean isKingInCheck(Color color) {
        // Find the king's position
        int kingRow = -1;
        int kingCol = -1;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board.getPiece(r, c);
                if (piece instanceof King && piece.getColor() == color) {
                    kingRow = r;
                    kingCol = c;
                    break;
                }
            }
            if (kingRow != -1) break;
        }

        // Check if any opponent piece can capture the king
        Color opponentColor = (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board.getPiece(r, c);
                if (piece != null && piece.getColor() == opponentColor) {
                    if (piece.canMove(board, r, c, kingRow, kingCol) &&
                            isPathClear(r, c, kingRow, kingCol)) {
                        return true; // The king is in check
                    }
                }
            }
        }

        return false; // The king is not in check
    }

    /**
     * Checks if player with specified color has any legal moves
     */
    private boolean hasLegalMoves(Color color) {
        // Try all possible moves for all pieces of that color
        for (int srcRow = 0; srcRow < 8; srcRow++) {
            for (int srcCol = 0; srcCol < 8; srcCol++) {
                Piece piece = board.getPiece(srcRow, srcCol);

                // Skip empty squares and opponent pieces
                if (piece == null || piece.getColor() != color) {
                    continue;
                }

                // Try all possible destinations for this piece
                for (int destRow = 0; destRow < 8; destRow++) {
                    for (int destCol = 0; destCol < 8; destCol++) {
                        // Create a move object to test
                        Move testMove = new Move(piece, srcRow, srcCol, destRow, destCol);

                        // If any valid move exists, player is not in checkmate/stalemate
                        if (isValidMove(testMove)) {
                            return true;
                        }
                    }
                }
            }
        }

        // No legal moves found
        return false;
    }
    
    private boolean isPathClear(int srcRow, int srcCol, int destRow, int destCol) {
        // If the piece is a knight, we don't need to check the path
        Piece piece = board.getPiece(srcRow, srcCol);

        if (piece instanceof Knight) {
            return true;
        }
        
        // Determine direction of movement
        int rowDirection = (destRow > srcRow) ? 1 : (destRow < srcRow) ? -1 : 0;
        int colDirection = (destCol > srcCol) ? 1 : (destCol < srcCol) ? -1 : 0;
        
        int row = srcRow + rowDirection;
        int col = srcCol + colDirection;
        
        // Check each square along the path (excluding the destination)
        while (row != destRow || col != destCol) {
            if (board.getPiece(row, col) != null) {
                return false; // Path is blocked
            }
            row += rowDirection;
            col += colDirection;
        }
        
        return true; // Path is clear
    }

    private boolean leavesKingInCheck(Move move, Color color) {
        // Create a temporary copy of the board to simulate the move
        Board tempBoard = new Board(); //new Board(board); // Assume Board has a copy constructor

        // Execute the move on the temporary board
        tempBoard.putPiece(move.getDestRow(), move.getDestCol(), move.getPiece());
        tempBoard.putPiece(move.getSrcRow(), move.getSrcCol(), null);

        // Find the king's position
        int kingRow = -1;
        int kingCol = -1;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = tempBoard.getPiece(r, c);
                if (piece instanceof King && piece.getColor() == color) {
                    kingRow = r;
                    kingCol = c;
                    break;
                }
            }
            if (kingRow != -1) break;
        }

        // Check if any opponent piece can capture the king
        Color opponentColor = (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = tempBoard.getPiece(r, c);
                if (piece != null && piece.getColor() == opponentColor) {
                    if (piece.canMove(tempBoard, r, c, kingRow, kingCol) &&
                            isPathClear(r, c, kingRow, kingCol)) {
                        return true; // The king would be in check
                    }
                }
            }
        }

        return false; // The king is not in check
    }
}