package org.sundeep.atm.machine.state;

import org.sundeep.atm.machine.Atm;
import org.sundeep.atm.models.Card;

public class IdleAtmState extends AtmState {
    public IdleAtmState(Atm atm) {
        super(atm);
    }

    @Override
    public void insertCard(Card card) throws UnsupportedOperationException {
        atm.setCurState(atm.hasCardAtmState());
    }
}
