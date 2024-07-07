package org.sundeep.elevator.controllers;

import org.sundeep.elevator.models.Direction;
import org.sundeep.elevator.models.ElevatorCar;

import java.util.ArrayList;
import java.util.List;

class ElevatorDispatcher {
	private List<ElevatorController> elevatorControllers = new ArrayList<>();

	public ElevatorDispatcher(int nElevators) {
		for (int i = 0; i < nElevators; i++) {
			elevatorControllers.add(new ElevatorController(new ElevatorCar()));
		}
	}

	public InternalButton callElevator(int floorId, Direction direction) {
		// TODO: Implement elevator scheduling algorithm.
		return null;
	}

	public void moveElevator(int elevatorId, int toFloor) {
		// make request to move the elevator
	}
}
