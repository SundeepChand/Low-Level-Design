package org.sundeep.tictactoe.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Player {
    private final String name;
    private final PieceType pieceType;

    public String toString() {
        return this.name;
    }
}
