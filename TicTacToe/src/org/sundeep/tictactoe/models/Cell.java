package org.sundeep.tictactoe.models;

import lombok.Getter;
import org.sundeep.tictactoe.exceptions.CellOccupiedException;

@Getter
public class Cell {
    private final int id;
    private boolean isEmpty;
    private Player player;

    Cell(int id) {
        this.id = id;
        this.isEmpty = true;
        this.player = null;
    }

    public Player clear() {
        isEmpty = true;
        Player curPlayer = player;
        player = null;
        return curPlayer;
    }

    public void setPiece(Player player) throws CellOccupiedException {
        if (!this.isEmpty) {
            throw new CellOccupiedException("Cell already occupied by " + this.player);
        }
        isEmpty = false;
        this.player = player;
    }

    public String toString() {
        if (this.player == null) {
            return " ";
        }
        return this.player.getPieceType().toString();
    }
}
