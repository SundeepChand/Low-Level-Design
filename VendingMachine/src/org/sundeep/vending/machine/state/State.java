package org.sundeep.vending.machine.state;

import org.sundeep.vending.machine.Item;
import org.sundeep.vending.machine.VendingMachine;
import org.sundeep.vending.machine.state.exceptions.OperationNotApplicableException;

public abstract class State {
    protected final VendingMachine vendingMachine;
    public State(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    public abstract void insertCash(double amount) throws OperationNotApplicableException;

    public abstract void moveToProductSelect() throws OperationNotApplicableException;

    public Item makeProductPurchase(String id) throws Exception {
        throw new OperationNotApplicableException("move to product select page first");
    }

    public void checkout() {
        vendingMachine.processRefundBalance();
        vendingMachine.setState(vendingMachine.getIdleState());
    }
}
