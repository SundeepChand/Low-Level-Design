package org.sundeep.zoomcar.service;

import org.sundeep.zoomcar.models.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ReservationManager {
	private final List<Reservation> reservations = new ArrayList<>();

	public Reservation findActiveReservationForUser(String userId) {
		for (Reservation reservation: reservations) {
			if (
					reservation.getUser().getId().equals(userId) &&
					reservation.getReservationStatus() == ReservationStatus.ACTIVE
			) {
				return reservation;
			}
		}
		return null;
	}

	public String createReservation(User user, Vehicle vehicle, Date from, Date to) {
		String id = UUID.randomUUID().toString();
		vehicle.setAvailability(AvailabilityStatus.UNAVAILABLE);
		reservations.add(new Reservation(
				id,
				user,
				vehicle,
				from,
				to,
				ReservationStatus.ACTIVE,
				new Invoice(
						((double) Math.abs(to.getTime() - from.getTime()) * 200 / 3600000) + 1000,
						PaymentStatus.PENDING
				)
		));
		return id;
	}

	public void updateReservationStatus(String id, ReservationStatus newStatus) {
		reservations.get(0).setReservationStatus(newStatus);
	}

	public Reservation getReservation(String id) {
		for (Reservation r: reservations) {
			if (r.getId().equals(id)) {
				return r;
			}
		}
		return null;
	}

	public void markReservationComplete(String id) {
		for (Reservation r: reservations) {
			if (r.getId().equals(id)) {
				r.getInvoice().setPaymentStatus(PaymentStatus.PAID);
				r.setReservationStatus(ReservationStatus.COMPLETE);
				r.getVehicle().setAvailability(AvailabilityStatus.AVAILABLE);
			}
		}
	}

	public List<Reservation> findAllReservationsOfUser(User user) {
		List<Reservation> reservationsOfUser = new ArrayList<>();
		for (Reservation r: reservations) {
			if (r.getUser().getId().equals(user.getId())) {
				reservationsOfUser.add(r);
			}
		}
		return reservationsOfUser;
	}
}
