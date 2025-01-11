package org.sundeep.vending.machine.state;

import org.sundeep.vending.machine.VendingMachine;
import org.sundeep.vending.machine.state.exceptions.OperationNotApplicableException;

public class IdleState extends State {

    public IdleState(VendingMachine vendingMachine) {
        super(vendingMachine);
    }

    @Override
    public void insertCash(double amount) throws OperationNotApplicableException {
        vendingMachine.setAvailableBalance(vendingMachine.getAvailableBalance() + amount);
        vendingMachine.setState(vendingMachine.getHasMoneyState());
    }

    @Override
    public void moveToProductSelect() throws OperationNotApplicableException {
        throw new OperationNotApplicableException("cannot select start selecting products in idle state");
    }
}
