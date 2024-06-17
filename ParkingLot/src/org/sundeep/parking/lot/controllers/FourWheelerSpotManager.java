package org.sundeep.parking.lot.controllers;

import org.sundeep.parking.lot.models.FourWheelerSpot;
import org.sundeep.parking.lot.models.ParkingSpot;

import java.util.ArrayList;
import java.util.List;

class FourWheelerSpotManager extends ParkingSpotManager {
    public FourWheelerSpotManager(int numSpots) {
        List<ParkingSpot> parkingSpots = new ArrayList<>();
        for (int i = 0; i < numSpots; i++) {
            parkingSpots.add(new FourWheelerSpot(i, 10));
        }
        this.setParkingSpotsList(parkingSpots);
    }
}
