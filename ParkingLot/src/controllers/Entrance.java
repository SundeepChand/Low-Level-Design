package controllers;

import exceptions.NoParkingSpaceAvailableException;
import exceptions.ParkingSpotOccupiedException;
import models.ParkingSpot;
import models.Ticket;
import models.Vehicle;

import java.time.LocalDateTime;

public class Entrance {
    private final ParkingSpotManagerFactory parkingSpotManagerFactory;

    public Entrance(ParkingSpotManagerFactory psmf) {
        this.parkingSpotManagerFactory = psmf;
    }

    public Ticket addVehicle(Vehicle vehicle)
            throws NoParkingSpaceAvailableException, ParkingSpotOccupiedException {
        ParkingSpotManager parkingSpotMgr = parkingSpotManagerFactory
                .getParkingSpotManager(vehicle.getVehicleType());

        ParkingSpot parkingSpot = parkingSpotMgr.findParkingSpot();
        if (parkingSpot == null) {
            throw new NoParkingSpaceAvailableException("no parking space available");
        }

        parkingSpot.parkVehicle(vehicle);
        return new Ticket(vehicle, parkingSpot, LocalDateTime.now());
    }
}
