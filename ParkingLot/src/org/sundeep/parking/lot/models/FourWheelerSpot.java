package org.sundeep.parking.lot.models;

public class FourWheelerSpot extends ParkingSpot {
    public FourWheelerSpot(int id, double price) {
        super(id, VehicleType.CAR, price);
    }
}