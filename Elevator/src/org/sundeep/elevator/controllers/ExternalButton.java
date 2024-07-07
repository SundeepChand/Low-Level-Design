package org.sundeep.elevator.controllers;

import org.sundeep.elevator.models.Direction;

public class ExternalButton {
	private final int floorId;
	private final ElevatorDispatcher elevatorDispatcher;

	public ExternalButton(int floorId, ElevatorDispatcher dispatcher) {
		this.floorId = floorId;
		this.elevatorDispatcher = dispatcher;
	}

	public InternalButton callElevator(Direction direction) {
		return elevatorDispatcher.callElevator(floorId, direction);
	}
}
