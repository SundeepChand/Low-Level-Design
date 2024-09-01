package org.sundeep.snake.and.ladder;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Board {
	private final Cell[] cells;
	private final Queue<Player> players;
	private final Dice[] dices;

	private final List<Player> winningPlayers;

	public Board(int nCells, int nDices, List<Portal> portalList, List<Player> players) throws InvalidParameterException {
		if (nCells == 0 || nDices == 0 || players.size() == 0) {
			throw new InvalidParameterException("The game parameters cannot be 0");
		}
		dices = new Dice[nDices];
		for (int i = 0; i < nDices; i++) {
			dices[i] = new Dice();
		}

		cells = new Cell[nCells];
		for (int i = 0; i < nCells; i++) {
			cells[i] = new Cell(i, null);
		}

		for (Portal portal: portalList) {
			if (
					portal.getStart() < 0 || portal.getStart() >= nCells ||
					portal.getEnd() < 0 || portal.getEnd() >= nCells
			) {
				System.out.println("WARN: " + portal + " out of bounds for board with " + nCells + " cells");
			} else {
				cells[portal.getStart()].setPortal(portal);
			}
		}

		this.players = new LinkedList<>();
		this.players.addAll(players);

		this.winningPlayers = new ArrayList<>();
	}

	void makeMove() {
		// Take the first player from the queue and move it to the end.
		Player curPlayer = this.players.poll();
		this.players.add(curPlayer);

		// Roll the dice
		int diceResult = 0;
		for (Dice dice: this.dices) {
			diceResult += dice.roll();
		}
		System.out.println(curPlayer.getName() + ": rolled dice " + diceResult);

		// Update the position of the player
		int finalPos = curPlayer.getCurPosition() + diceResult;
		if (finalPos >= this.cells.length) {
			// If the dice throw results in the player going out of board, then it is a No-op.
			return;
		}
		curPlayer.setPosition(finalPos);
		while (cells[finalPos].getPortal() != null) {
			cells[finalPos].getPortal().transport(curPlayer);
			finalPos = curPlayer.getCurPosition();
		}
		System.out.println(curPlayer.getName() + ": moved to position " + curPlayer.getCurPosition());

		if (finalPos == cells.length - 1) {
			winningPlayers.add(curPlayer);
		}
	}

	public List<Player> getWinningPlayers() {
		return this.winningPlayers;
	}
}
