package org.sundeep.atm.machine.state;

import org.sundeep.atm.exceptions.InvalidCredentialException;
import org.sundeep.atm.machine.Atm;
import org.sundeep.atm.models.Card;
import org.sundeep.zoomcar.models.User;

public abstract class AtmState {
    protected Atm atm;

    public AtmState(Atm atm) {
        this.atm = atm;
    }

    public void insertCard(Card card) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("insert card operation not supported in this state");
    }

    public void authenticatePin(String pin) throws UnsupportedOperationException, InvalidCredentialException {
        throw new UnsupportedOperationException("pin authentication not supported in this state");
    }

    public void performOperation() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("perform operation not supported in this state");
    }

    public void reset() {
        atm.resetCard();
        atm.setCurState(atm.getIdleAtmState());
    }
}
