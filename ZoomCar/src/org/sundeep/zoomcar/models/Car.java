package org.sundeep.zoomcar.models;

import lombok.AllArgsConstructor;

public class Car extends Vehicle {
	public Car(String vehicleNumer, int distance) {
		super(vehicleNumer, VehicleType.CAR, distance, AvailabilityStatus.AVAILABLE);
	}
}
