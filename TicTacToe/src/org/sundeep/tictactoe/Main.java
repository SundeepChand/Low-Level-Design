package org.sundeep.tictactoe;

import org.sundeep.tictactoe.view.TicTacToeGame;

public class Main {
    public static void main(String[] args) {
        TicTacToeGame game = new TicTacToeGame(3);
        game.start();
    }
}