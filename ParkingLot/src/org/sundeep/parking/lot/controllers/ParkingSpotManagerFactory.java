package org.sundeep.parking.lot.controllers;

import org.sundeep.parking.lot.models.VehicleType;

public class ParkingSpotManagerFactory {
    private final TwoWheelerSpotManager bikeSpotManager;
    private final FourWheelerSpotManager carSpotManager;

    public ParkingSpotManagerFactory(int numBikeSpots, int numCarSpots) {
        this.bikeSpotManager = new TwoWheelerSpotManager(numBikeSpots);
        this.carSpotManager = new FourWheelerSpotManager(numCarSpots);
    }

    public ParkingSpotManager getParkingSpotManager(VehicleType type) {
        return switch (type) {
            case CAR -> this.carSpotManager;
            case BIKE -> this.bikeSpotManager;
        };
    }
}
