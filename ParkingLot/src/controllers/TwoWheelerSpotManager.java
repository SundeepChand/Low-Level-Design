package controllers;

import models.ParkingSpot;
import models.TwoWheelerSpot;

import java.util.ArrayList;
import java.util.List;

class TwoWheelerSpotManager extends ParkingSpotManager {
    public TwoWheelerSpotManager(int numSpots) {
        List<ParkingSpot> parkingSpots = new ArrayList<>();
        for (int i = 0; i < numSpots; i++) {
            parkingSpots.add(new TwoWheelerSpot(i, 10));
        }
        this.setParkingSpotsList(parkingSpots);
    }
}
