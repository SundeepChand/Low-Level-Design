package controllers;

import models.FourWheelerSpot;
import models.ParkingSpot;
import models.TwoWheelerSpot;

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
