package org.sundeep.parking.lot.exceptions;

public class NoParkingSpaceAvailableException extends Exception {
    public NoParkingSpaceAvailableException(String message) {
        super(message);
    }
}
