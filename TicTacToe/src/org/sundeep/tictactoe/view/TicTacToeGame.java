package org.sundeep.tictactoe.view;

import org.sundeep.tictactoe.controller.Game;
import org.sundeep.tictactoe.exceptions.CellOccupiedException;
import org.sundeep.tictactoe.models.Cell;
import org.sundeep.tictactoe.models.PieceType;
import org.sundeep.tictactoe.models.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

enum GameState {
    HOME,
    GAME_STARTED,
    QUIT,
}

public class TicTacToeGame {
    private final int boardSize;
    private final Scanner inputScanner = new Scanner(System.in);

    public TicTacToeGame(int boardSize) {
        this.boardSize = boardSize;
    }

    public void start() {
        GameState curGameState = GameState.HOME;
        boolean shouldQuit = false;
        while (!shouldQuit) {
            switch (curGameState) {
                case HOME -> curGameState = handleShowHome(curGameState);
                case GAME_STARTED -> curGameState = handleGameStart(curGameState);
                case QUIT -> shouldQuit = true;
            }
        }
    }

    private void showTitle() {
        System.out.println("\tTic-Tac-Toe");
    }

    private GameState handleShowHome(GameState curState) {
        showTitle();
        System.out.println("1. New Game");
        System.out.println("2. Quit");
        System.out.print("Enter your choice: ");

        try {
            String input = inputScanner.nextLine();
            int choice = Integer.parseInt(input);
            switch (choice) {
                case 1:
                    return GameState.GAME_STARTED;
                case 2:
                    return GameState.QUIT;
                default:
                    System.out.println("Invalid choice");
            }
        } catch (Exception e) {
            System.out.println("Invalid choice: " + e.getMessage());
        }
        return curState;
    }

    private GameState handleGameStart(GameState curState) {
        showTitle();

        List<Player> playersList = new LinkedList<>();

        System.out.print("Enter player 1 name: ");
        String name1 = inputScanner.nextLine();
        Player p1 = new Player(name1, PieceType.O);
        playersList.add(p1);

        System.out.print("Enter player 2 name: ");
        String name2 = inputScanner.nextLine();
        Player p2 = new Player(name2, PieceType.X);
        playersList.add(p2);

        Game game = new Game(boardSize, playersList);
        Player winner = null;
        boolean isTie = false;
        while (!isTie && winner == null) {
            if (game.isTie()) {
                isTie = true;
                continue;
            }

            printBoard(game);
            Player curPlayer = game.getNextPlayer();
            System.out.print(curPlayer + " enter your position (row col): ");

            try {
                String input = inputScanner.nextLine();
                int row = Integer.parseInt(input.split(" ")[0]);
                int col = Integer.parseInt(input.split(" ")[1]);
                winner = game.makeMove(row, col);
            } catch (CellOccupiedException e) {
                System.out.println("Cell already occupied");
            } catch (Exception e) {
                System.out.println("Invalid input. " + e.getMessage());
            }
        }
        if (isTie) {
            System.out.println("Game ends in a tie");
        } else {
            printBoard(game);
            System.out.println(winner + " wins the game");
        }
        return GameState.HOME;
    }

    private void printBoard(Game game) {
        Cell[][] board = game.getBoard().getState();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (j > 0) {
                    System.out.print("|");
                }
                System.out.print(" " + board[i][j] + " ");
            }
            System.out.println();
            for (int j = 0; i < boardSize - 1 && j < boardSize; j++) {
                System.out.print("--- ");
            }
            System.out.println();
        }
    }
}
