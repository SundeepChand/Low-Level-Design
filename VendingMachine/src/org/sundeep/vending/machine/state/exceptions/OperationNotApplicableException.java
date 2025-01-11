package org.sundeep.vending.machine.state.exceptions;

public class OperationNotApplicableException extends Exception {
    public OperationNotApplicableException() {}

    public OperationNotApplicableException(String msg) {
        super(msg);
    }
}
