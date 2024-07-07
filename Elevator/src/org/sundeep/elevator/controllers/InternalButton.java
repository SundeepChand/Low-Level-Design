package org.sundeep.elevator.controllers;

public class InternalButton {
	private int nFloors;
	ElevatorDispatcher elevatorDispatcher;

	public InternalButton(int nFloors, ElevatorDispatcher elevatorDispatcher) {
		this.nFloors = nFloors;
		this.elevatorDispatcher = elevatorDispatcher;
	}

	public void moveElevator(int toFloor) {

	}
}
