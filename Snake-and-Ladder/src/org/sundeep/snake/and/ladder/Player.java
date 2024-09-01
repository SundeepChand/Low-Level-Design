package org.sundeep.snake.and.ladder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class Player implements Movable {
	private int curPosition;
	private final String name;

	public Player(String name) {
		this.name = name;
		this.curPosition = 0;
	}

	@Override
	public void setPosition(int value) {
		curPosition = value;
	}
}
