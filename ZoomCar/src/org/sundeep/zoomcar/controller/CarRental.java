package org.sundeep.zoomcar.controller;

import lombok.AllArgsConstructor;
import org.sundeep.zoomcar.models.Location;
import org.sundeep.zoomcar.models.Reservation;
import org.sundeep.zoomcar.models.Store;
import org.sundeep.zoomcar.models.User;
import org.sundeep.zoomcar.service.StoreManager;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CarRental {
	private List<User> users;
	private List<StoreManager> stores;

	public StoreManager findNearestStore(Location location) {
		for (StoreManager sm: stores) {
			// TODO: currently performing exact match
			if (
					sm.getStore().getLocation().getAddress().equals(location.getAddress()) &&
					sm.getStore().getLocation().getCity().equals(location.getCity())
			) {
				return sm;
			}
		}
		return null;
	}

	public List<Reservation> getReservationsOfUser(User user) {
		List<Reservation> reservations = new ArrayList<>();
		for (StoreManager sm: stores) {
			reservations.addAll(sm.getReservationsOfUser(user));
		}
		return reservations;
	}
}
