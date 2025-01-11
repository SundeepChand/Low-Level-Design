package org.sundeep.vending.machine.state.exceptions;

public class ProductNotAvailableException extends Exception {
    public ProductNotAvailableException() {}

    public ProductNotAvailableException(String msg) {
        super(msg);
    }
}
