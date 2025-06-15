package com.lld.chess.core;

import com.lld.chess.exception.InvalidMoveException;
import com.lld.chess.model.Move;
import com.lld.chess.pieces.Piece;

import java.util.Scanner;

public class ChessGame {
    private final Board board;
    private final MoveValidator moveValidator;
    private Player[] players;
    private int currentPlayer;

    public ChessGame() {
        this.board = new Board();
        this.moveValidator = new MoveValidator(board);
    }

    public void initialiseGame(Player whitePlayer, Player blackPlayer) {
        this.board.putPiecesOnBoard();
        this.players = new Player[] { whitePlayer, blackPlayer };
        this.currentPlayer = 0;
    }

    public void start() {
        // Game loop
        while (!moveValidator.isGameOver()) {
            Player player = players[currentPlayer];

            System.out.println(player.getColor() + "'s turn.");

            try {
                // Get move from the player
                Move move = getPlayerMove(player);

                // Validate move
                if (moveValidator.isValidMove(move)) {

                    // Make the move on the board
                    makeMove(board, move);
                }
            } catch (InvalidMoveException e) {
                System.out.println("Try again!" + e.getMessage());
                continue;
            }

            // Switch to the next player
            currentPlayer = (currentPlayer + 1) % 2;
        }
    }

    private Move getPlayerMove(Player player) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter source row: ");
        int sourceRow = scanner.nextInt();
        System.out.print("Enter source column: ");
        int sourceCol = scanner.nextInt();
        System.out.print("Enter destination row: ");
        int destRow = scanner.nextInt();
        System.out.print("Enter destination column: ");
        int destCol = scanner.nextInt();

        Piece piece = board.getPiece(sourceRow, sourceCol);

        if (piece == null || piece.getColor() != player.getColor()) {
            throw new IllegalArgumentException("Invalid piece selection!");
        }

        return new Move(piece, sourceRow, sourceCol, destRow, destCol);
    }

    public void makeMove(Board board, Move move) {
        Piece piece = move.getPiece();

        int srcRow = move.getSrcRow();
        int srcCol = move.getSrcCol();

        int destRow = move.getDestRow();
        int destCol = move.getDestCol();

        // Lift current piece
        board.removePiece(srcRow, srcCol);

        // Kill if there is already some piece on destination
        Piece destPiece = board.getPiece(destRow, destCol);
        if (destPiece != null) {
            killPiece(board, destRow, destCol);
        }

        // Put piece on destination
        board.putPiece(destRow, destCol, piece);
    }

    private void killPiece(Board board, int row, int col) {
        Piece piece = board.getPiece(row, col);
        board.removePiece(row, col);
        piece.setActive(false);
    }

}
