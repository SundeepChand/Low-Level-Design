package org.sundeep.zoomcar.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class Reservation {
	private String id;
	private User user;
	private Vehicle vehicle;
	private Date fromTime;
	private Date tillTime;
	private ReservationStatus reservationStatus;
	private Invoice invoice;
}
