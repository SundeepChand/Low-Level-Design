package org.sundeep.elevator.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sundeep.elevator.controllers.ExternalButton;
import org.sundeep.elevator.models.Floor;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Building {
	private final List<Floor> floorList = new ArrayList<>();
	private ElevatorDispatcher elevatorDispatcher;

	public Building(int nFloors, int nElevators) {
		this.elevatorDispatcher = new ElevatorDispatcher(nElevators);
		for (int i = 0; i < nFloors; i++) {
			floorList.add(new Floor(i, new ExternalButton(i, elevatorDispatcher)));
		}
	}

	public Floor getFloor(int floorId) throws IllegalArgumentException {
		if (floorId >= floorList.size()) {
			throw new IllegalArgumentException("non-existent floor ID");
		}
		return floorList.get(floorId);
	}
}
