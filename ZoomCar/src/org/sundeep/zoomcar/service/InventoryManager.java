package org.sundeep.zoomcar.service;

import lombok.AllArgsConstructor;
import org.sundeep.zoomcar.models.AvailabilityStatus;
import org.sundeep.zoomcar.models.Vehicle;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class InventoryManager {
	private final List<Vehicle> vehicles = new ArrayList<>();

	public void addVehicle(Vehicle vehicle) {
		vehicles.add(vehicle);
	}

	public Vehicle suggestRandomVehicle() {
		// TODO: Add logic to search for vehicle here
		return vehicles.get(0);
	}

	public Vehicle findVehicle(String id) {
		for (Vehicle vehicle: vehicles) {
			if (vehicle.getNumber().equals(id)) {
				return vehicle;
			}
		}
		return null;
	}

	public void updateVehicleAvailability(String vehicleNo, AvailabilityStatus newStatus) {
		for (Vehicle v: vehicles) {
			if (v.getNumber().equals(vehicleNo)) {
				v.setAvailability(newStatus);
			}
		}
	}
}
