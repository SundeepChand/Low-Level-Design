package org.sundeep.snake.and.ladder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public class Portal {
	private final int start, end;

	public void transport(@NotNull Movable movable) {
		System.out.println("Player moved to position: " + end);
		movable.setPosition(end);
	}

	@Override
	public String toString() {
		return "(" + start + ", " + end + ")";
	}
}
