package org.sundeep.parking.lot.controllers;

import org.sundeep.parking.lot.models.ParkingSpot;

import java.util.List;

abstract class ParkingSpotManager {
    private List<ParkingSpot> parkingSpotsList;

    protected void setParkingSpotsList(List<ParkingSpot> parkingSpotsList) {
        this.parkingSpotsList = parkingSpotsList;
    }

    public ParkingSpot findParkingSpot() {
        for (ParkingSpot p: parkingSpotsList) {
            if (p.isEmpty()) {
                return p;
            }
        }
        return null;
    }

// TODO: Implement fetching of parking spot based on criterion
//    public ParkingSpot findParkingSpot(Criterion c) {
//
//    }

    public void addParkingSpot(ParkingSpot p) {
        parkingSpotsList.add(p);
    }

    public void removeParkingSpot(int parkingSpotId) {
        parkingSpotsList.removeIf((ParkingSpot p) -> p.getId() == parkingSpotId);
    }
}
