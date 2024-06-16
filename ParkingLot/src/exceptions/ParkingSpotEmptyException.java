package exceptions;

public class ParkingSpotEmptyException extends Exception {
    public ParkingSpotEmptyException(String message) {
        super(message);
    }
}
