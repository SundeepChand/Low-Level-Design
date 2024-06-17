package org.sundeep.tictactoe.controller;

import org.sundeep.tictactoe.exceptions.CellOccupiedException;
import org.sundeep.tictactoe.models.Board;
import org.sundeep.tictactoe.models.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Game {
    private final Board board;
    private final Queue<Player> players;

    public Game(int boardSize, List<Player> playerList) {
        this.board = new Board(boardSize);
        this.players = new LinkedList<>(playerList);
    }

    public Board getBoard() {
        return this.board;
    }

    public Player getNextPlayer() {
        return this.players.peek();
    }

    public boolean isTie() {
        return this.board.areAllCellsFilled();
    }

    public Player makeMove(int row, int col) throws CellOccupiedException {
        Player curPlayer = this.players.peek();
        this.board.makePlayerMove(row, col, curPlayer);
        this.players.add(curPlayer);
        this.players.poll();
        return this.board.checkAndGetWinner();
    }
}
