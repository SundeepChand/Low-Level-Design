package org.sundeep.atm.machine.state;

import org.sundeep.atm.exceptions.InvalidCredentialException;
import org.sundeep.atm.machine.Atm;

public class HasCardAtmState extends AtmState {
    public HasCardAtmState(Atm atm) {
        super(atm);
    }

    @Override
    public void authenticatePin(String pin) throws UnsupportedOperationException, InvalidCredentialException {
        // Call some external service to validate the pin
        // In this case we'll simply store the pin with the card instance and check with that.
        if (!atm.getCard().getPin().equals(pin)) {
            reset();
            throw new InvalidCredentialException("the pin does not match");
        }
        atm.setCurState(atm.operationSelectionAtmState());
    }
}
