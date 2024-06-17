package org.sundeep.parking.lot.controllers;

import org.sundeep.parking.lot.exceptions.NoParkingSpaceAvailableException;
import org.sundeep.parking.lot.exceptions.ParkingSpotOccupiedException;
import org.sundeep.parking.lot.models.ParkingSpot;
import org.sundeep.parking.lot.models.Ticket;
import org.sundeep.parking.lot.models.Vehicle;

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
