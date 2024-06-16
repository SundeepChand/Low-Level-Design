package models;

import exceptions.ParkingSpotEmptyException;
import exceptions.ParkingSpotOccupiedException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public abstract class ParkingSpot {
    private int id;
    private boolean isEmpty = true;
    private Vehicle vehicle = null;
    private VehicleType spotType;
    private double fixedPrice;

    public ParkingSpot(int id, VehicleType spotType, double fixedPrice) {
        this.id = id;
        this.spotType = spotType;
        this.fixedPrice = fixedPrice;
    }

    public void parkVehicle(Vehicle vehicle) throws ParkingSpotOccupiedException {
        if (!this.isEmpty) {
            throw new ParkingSpotOccupiedException(this.id + " parking spot already occupied");
        }
        this.vehicle = vehicle;
        this.isEmpty = false;
    }

    public Vehicle removeVehicle() throws ParkingSpotEmptyException {
        if (this.isEmpty) {
            throw new ParkingSpotEmptyException(this.id + " parking spot already empty");
        }
        Vehicle toReturn = this.vehicle;
        this.vehicle = null;
        this.isEmpty = true;
        return toReturn;
    }
}
