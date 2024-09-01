package org.sundeep.snake.and.ladder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Cell {
	private final int id;
	private Portal portal;

	public void setPortal(Portal portal) {
		this.portal = portal;
	}
}
