package org.sundeep.elevator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sundeep.elevator.controllers.ExternalButton;
import org.sundeep.elevator.controllers.InternalButton;

@Getter
@AllArgsConstructor
public class Floor {
	private int floorId;
	private ExternalButton externalButton;

	public InternalButton callElevator(Direction direction) {
		return this.externalButton.callElevator(direction);
	}
}
