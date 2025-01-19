package org.sundeep.atm.machine.processor;

import org.sundeep.atm.machine.Atm;
import org.sundeep.atm.models.Card;

public class OperationDto {
    private final Atm atm;
    private final Card card;

    public OperationDto(Atm atm, Card card) {
        this.atm = atm;
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public Atm getAtm() {
        return atm;
    }
}
