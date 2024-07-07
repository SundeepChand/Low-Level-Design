package org.sundeep.elevator.models;

import lombok.Getter;
import lombok.Setter;
import org.sundeep.elevator.exceptions.ElevatorOverloadedException;

@Getter
@Setter
public class ElevatorCar {
	private int elevatorId;
	private int curFloor;
	private ElevatorStatus elevatorStatus;
	private boolean isOverloaded = false;

	public void move(int toFloor) throws ElevatorOverloadedException {
		if (isOverloaded) {
			throw new ElevatorOverloadedException("cannot move overloaded elevator");
		}
		this.curFloor = toFloor;
	}
}
