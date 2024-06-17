package org.sundeep.parking.lot.models;

public class TwoWheelerSpot extends ParkingSpot {
    public TwoWheelerSpot(int id, double price) {
        super(id, VehicleType.BIKE, price);
    }
}
