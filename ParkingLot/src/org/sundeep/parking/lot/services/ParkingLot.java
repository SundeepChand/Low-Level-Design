package org.sundeep.parking.lot.services;

import org.sundeep.parking.lot.controllers.Entrance;
import org.sundeep.parking.lot.controllers.ExitGate;
import org.sundeep.parking.lot.controllers.ParkingSpotManagerFactory;
import org.sundeep.parking.lot.exceptions.NoParkingSpaceAvailableException;
import org.sundeep.parking.lot.exceptions.ParkingSpotEmptyException;
import org.sundeep.parking.lot.exceptions.ParkingSpotOccupiedException;
import org.sundeep.parking.lot.models.Ticket;
import org.sundeep.parking.lot.models.Vehicle;

public class ParkingLot {
    private final Entrance entrance;
    private final ExitGate exitGate;

    public ParkingLot(int numBikeSpots, int numCarSpots) {
        ParkingSpotManagerFactory parkingSpotManagerFactory = new ParkingSpotManagerFactory(numBikeSpots, numCarSpots);
        this.entrance = new Entrance(parkingSpotManagerFactory);
        this.exitGate = new ExitGate(parkingSpotManagerFactory);
    }

    public Ticket addVehicle(Vehicle vehicle) {
        try {
            return this.entrance.addVehicle(vehicle);
        } catch (NoParkingSpaceAvailableException e) {
            System.out.println("Unable to park vehicle. " + e.getMessage());
            e.printStackTrace();
        } catch (ParkingSpotOccupiedException e) {
            System.out.println("Internal error. " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void checkOutVehicle(Ticket ticket) {
        try {
            this.exitGate.checkoutVehicle(ticket);
        } catch (ParkingSpotEmptyException e) {
            System.out.println("Internal error. " + e.getMessage());
            e.printStackTrace();
        }
    }
}
