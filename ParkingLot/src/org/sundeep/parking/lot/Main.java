package org.sundeep.parking.lot;

import org.sundeep.parking.lot.models.Car;
import org.sundeep.parking.lot.models.Ticket;
import org.sundeep.parking.lot.models.Vehicle;
import org.sundeep.parking.lot.services.ParkingLot;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ParkingLot parkingLot = new ParkingLot(5, 10);

        Vehicle car1 = new Car("OD-01R-2932");
        Ticket ticket = parkingLot.addVehicle(car1);
        if (ticket == null) {
            return;
        }

        Thread.sleep(1000 * 72);

        parkingLot.checkOutVehicle(ticket);
        System.out.println("Successfully checked out " + ticket);
    }
}