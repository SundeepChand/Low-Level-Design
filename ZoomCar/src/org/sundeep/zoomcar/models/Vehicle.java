package org.sundeep.zoomcar.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
abstract public class Vehicle {
	private String number;
	private VehicleType type;
	private int kilometersRun;
	private AvailabilityStatus availability;
}
