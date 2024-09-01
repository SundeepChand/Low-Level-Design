package org.sundeep.snake.and.ladder;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Dice {
	private final int sides;

	public Dice() {
		this.sides = 6;
	}

	public int roll() {
		return 1 + (int)(Math.random() * this.sides);
	}
}
