package org.sundeep.parking.lot.controllers;

import org.sundeep.parking.lot.exceptions.ParkingSpotEmptyException;
import org.sundeep.parking.lot.models.Ticket;

import java.time.LocalDateTime;

public class ExitGate {
    ParkingSpotManagerFactory parkingSpotManagerFactory;

    public ExitGate(ParkingSpotManagerFactory psmf) {
        this.parkingSpotManagerFactory = psmf;
    }

    public void checkoutVehicle(Ticket ticket) throws ParkingSpotEmptyException {
        ticket.setExitTime(LocalDateTime.now());
        ticket.calculateTotalAmount();
        ticket.getParkingSpot().removeVehicle();
    }
}
