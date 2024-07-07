package org.sundeep.elevator.controllers;

import lombok.AllArgsConstructor;
import org.sundeep.elevator.exceptions.ElevatorOverloadedException;
import org.sundeep.elevator.models.ElevatorCar;

@AllArgsConstructor
class ElevatorController {
	private ElevatorCar elevatorCar;

	public void moveElevator(int toFloor) throws ElevatorOverloadedException {
		this.elevatorCar.move(toFloor);
	}
}
