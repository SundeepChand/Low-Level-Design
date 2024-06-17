package org.sundeep.tictactoe.models;

import org.sundeep.tictactoe.exceptions.CellOccupiedException;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Cell> cells;

    private final int boardSize;

    public Board(int size) {
        boardSize = size;
        this.resetBoard();
    }

    public void resetBoard() {
        cells = new ArrayList<>();
        for (int i = 0; i < this.boardSize * this.boardSize; i++) {
            cells.add(new Cell(i));
        }
    }

    public void makePlayerMove(int row, int col, Player player) throws CellOccupiedException {
        this.cells.get(coordinatesToCellIndex(row, col)).setPiece(player);
    }

    private int coordinatesToCellIndex(int r, int c) {
        return boardSize * r + c;
    }

    public boolean areAllCellsFilled() {
        for (Cell c: this.cells) {
            if (c.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public Player checkAndGetWinner() {
        // Check for each row
        for (int i = 0; i < boardSize; i++) {
            Player firstPlayer = this.cells.get(coordinatesToCellIndex(i, 0)).getPlayer();
            boolean canWin = true;
            for (int j = 0; j < boardSize; j++) {
                Cell curCell = this.cells.get(coordinatesToCellIndex(i, j));
                if (curCell.getPlayer() != firstPlayer) {
                    canWin = false;
                    break;
                }
            }
            if (canWin) {
                return firstPlayer;
            }
        }

        // Check for each column
        for (int j = 0; j < boardSize; j++) {
            Player firstPlayer = this.cells.get(coordinatesToCellIndex(0, j)).getPlayer();
            boolean canWin = true;
            for (int i = 0; i < boardSize; i++) {
                Cell curCell = this.cells.get(coordinatesToCellIndex(i, j));
                if (curCell.getPlayer() != firstPlayer) {
                    canWin = false;
                    break;
                }
            }
            if (canWin) {
                return firstPlayer;
            }
        }

        // Check for diagonal
        Player firstPlayer = this.cells.get(coordinatesToCellIndex(0, 0)).getPlayer();
        boolean canWin = true;
        for (int i = 0; i < boardSize; i++) {
            Cell curCell = this.cells.get(coordinatesToCellIndex(i, i));
            if (curCell.getPlayer() != firstPlayer) {
                canWin = false;
                break;
            }
        }
        if (canWin) {
            return firstPlayer;
        }

        // Check for anti-diagonal
        firstPlayer = this.cells.get(coordinatesToCellIndex(0, boardSize - 1)).getPlayer();
        canWin = true;
        for (int i = 0; i < boardSize; i++) {
            Cell curCell = this.cells.get(coordinatesToCellIndex(i, boardSize - 1 - i));
            if (curCell.getPlayer() != firstPlayer) {
                canWin = false;
                break;
            }
        }
        if (canWin) {
            return firstPlayer;
        }
        return null;
    }

    public Cell[][] getState() {
        Cell[][] board = new Cell[this.boardSize][this.boardSize];
        int row = 0, col = 0;
        for (Cell c: this.cells) {
            board[row][col] = c;
            col++;
            if (col >= boardSize) {
                row++;
                col = 0;
            }
        }
        return board;
    }
}
