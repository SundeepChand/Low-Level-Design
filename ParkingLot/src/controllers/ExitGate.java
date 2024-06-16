package controllers;

import exceptions.ParkingSpotEmptyException;
import models.Ticket;

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
