package org.sundeep.atm.machine.state;

import org.sundeep.atm.machine.Atm;
import org.sundeep.atm.machine.processor.OperationDto;
import org.sundeep.atm.machine.processor.OperationProcessor;

public class OperationSelectionAtmState extends AtmState {
    private final OperationProcessor operationProcessor;

    public OperationSelectionAtmState(Atm atm, OperationProcessor operationProcessor) {
        super(atm);
        this.operationProcessor = operationProcessor;
    }

    public void performOperation() throws UnsupportedOperationException {
        operationProcessor.handleUserOperation(new OperationDto(atm, atm.getCard()));
        // Reset the ATM after performing the operation
        reset();
    }
}
