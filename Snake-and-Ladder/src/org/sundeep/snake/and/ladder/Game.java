package org.sundeep.snake.and.ladder;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private final Board board;

	public Game(Board board) {
		this.board = board;
	}

	public List<Player> autoRun() {
		List<Player> winningPlayers = new ArrayList<>();
		while (winningPlayers.size() == 0) {
			this.board.makeMove();
			winningPlayers = this.board.getWinningPlayers();
		}
		return winningPlayers;
	}
}
