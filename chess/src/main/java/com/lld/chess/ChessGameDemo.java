package com.lld.chess;

import com.lld.chess.core.ChessGame;
import com.lld.chess.core.Player;
import com.lld.chess.enums.Color;

public class ChessGameDemo {
    public static void run() {
        ChessGame chessGame = new ChessGame();

        Player whitePlayer = new Player("test-player-1", Color.WHITE);
        Player blackPlayer = new Player("test-player-2", Color.BLACK);
        chessGame.initialiseGame(whitePlayer, blackPlayer);

        chessGame.start();
    }
}
