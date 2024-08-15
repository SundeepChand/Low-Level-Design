package org.sundeep.zoomcar;

import org.sundeep.zoomcar.controller.CarRental;
import org.sundeep.zoomcar.models.*;
import org.sundeep.zoomcar.service.InventoryManager;
import org.sundeep.zoomcar.service.ReservationManager;
import org.sundeep.zoomcar.service.StoreManager;

import java.util.*;

public class Main {
	public static void main(String[] args) {
		Vehicle vehicle1 = new Car("JPY-9019", 100);

		User myUser = new User(UUID.randomUUID().toString(), "Sundeep", "ABC");
		List<User> userList = new ArrayList<>();
		userList.add(myUser);

		List<StoreManager> storeManagers = new ArrayList<>();

		StoreManager storeManager1 = new StoreManager(
				new InventoryManager(),
				new ReservationManager(),
				new Store(new Location("Shibuya", "Tokyo"))
		);
		storeManager1.addVehicle(vehicle1);
		storeManagers.add(storeManager1);

		CarRental carRental = new CarRental(userList, storeManagers);

		StoreManager myStore = carRental.findNearestStore(new Location("Shibuya", "Tokyo"));
		Vehicle myVehicle = myStore.findVehicle();

		String vehicleNumber = myStore.bookVehicle(myUser, myVehicle.getNumber(), new Date(2024, Calendar.AUGUST, 17), new Date(2024, Calendar.AUGUST, 18));

		System.out.println("Successfully booked vehicle. Reservation ID: " + vehicleNumber);
		System.out.println(
			myStore.getReservationsOfUser(myUser).get(0).getId() + ": " +
			myStore.getReservationsOfUser(myUser).get(0).getVehicle().getNumber() + ", " +
			myStore.getReservationsOfUser(myUser).get(0).getInvoice().getPrice() + " " +
			myStore.getReservationsOfUser(myUser).get(0).getInvoice().getPaymentStatus()
		);

		myStore.returnBookedVehicle(myUser);

		System.out.println(
			myStore.getReservationsOfUser(myUser).get(0).getId() + ": " +
			myStore.getReservationsOfUser(myUser).get(0).getVehicle().getNumber() + ", " +
			myStore.getReservationsOfUser(myUser).get(0).getInvoice().getPrice() + " " +
			myStore.getReservationsOfUser(myUser).get(0).getInvoice().getPaymentStatus()
		);
	}
}
