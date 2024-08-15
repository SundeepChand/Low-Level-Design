package org.sundeep.zoomcar.service;

import lombok.AllArgsConstructor;
import org.sundeep.zoomcar.models.Reservation;
import org.sundeep.zoomcar.models.Store;
import org.sundeep.zoomcar.models.User;
import org.sundeep.zoomcar.models.Vehicle;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class StoreManager {
	private InventoryManager inventoryManager;
	private ReservationManager reservationManager;
	private Store store;

	public Vehicle findVehicle() {
		return inventoryManager.suggestRandomVehicle();
	}

	public void addVehicle(Vehicle vehicle) {
		inventoryManager.addVehicle(vehicle);
	}

	public String bookVehicle(User user, String vehicleId, Date from, Date to) {
		Vehicle vehicle = inventoryManager.findVehicle(vehicleId);
		return reservationManager.createReservation(user, vehicle, from, to);
	}

	public Store getStore() {
		return store;
	}

	public void returnBookedVehicle(User user) {
		Reservation activeReservation = reservationManager.findActiveReservationForUser(user.getId());
		reservationManager.markReservationComplete(activeReservation.getId());
	}

	public List<Reservation> getReservationsOfUser(User user) {
		return reservationManager.findAllReservationsOfUser(user);
	}
}
