package org.sundeep.vending.machine.state;

import org.sundeep.vending.machine.VendingMachine;
import org.sundeep.vending.machine.state.exceptions.OperationNotApplicableException;

public class HasMoneyState extends State {
    public HasMoneyState(VendingMachine vendingMachine) {
        super(vendingMachine);
    }

    @Override
    public void insertCash(double amount) throws OperationNotApplicableException {
        vendingMachine.setAvailableBalance(vendingMachine.getAvailableBalance() + amount);
    }

    @Override
    public void moveToProductSelect() throws OperationNotApplicableException {
        vendingMachine.setState(vendingMachine.getProductSelectionState());
    }
}
