package org.sundeep.snake.and.ladder;

import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		List<Portal> portalList = new ArrayList<>();
		// Ladders
		portalList.add(new Portal(2, 52));
		portalList.add(new Portal(12, 43));
		portalList.add(new Portal(21, 92));
		portalList.add(new Portal(2, 52));
		portalList.add(new Portal(67, 82));
		portalList.add(new Portal(42, 54));

		// Snakes
		portalList.add(new Portal(96, 22));
		portalList.add(new Portal(64, 5));
		portalList.add(new Portal(78, 52));
		portalList.add(new Portal(34, 16));
		portalList.add(new Portal(88, 32));

		List<Player> playerList = new ArrayList<>();
		playerList.add(new Player("Player 0"));
		playerList.add(new Player("Player 1"));
		playerList.add(new Player("Player 2"));

		Board board = new Board(100, 1, portalList, playerList);
		Game game = new Game(board);
		List<Player> winners = game.autoRun();
		for (Player player: winners) {
			System.out.println(player.getName() + " wins!");
		}
	}
}
