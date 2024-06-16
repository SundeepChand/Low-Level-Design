import models.Car;
import models.Ticket;
import models.Vehicle;
import models.VehicleType;
import services.ParkingLot;

import java.util.Timer;

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