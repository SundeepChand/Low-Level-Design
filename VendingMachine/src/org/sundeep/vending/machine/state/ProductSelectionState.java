package org.sundeep.vending.machine.state;

import org.sundeep.vending.machine.Item;
import org.sundeep.vending.machine.VendingMachine;
import org.sundeep.vending.machine.state.exceptions.InsufficientBalanceException;
import org.sundeep.vending.machine.state.exceptions.OperationNotApplicableException;
import org.sundeep.vending.machine.state.exceptions.ProductNotAvailableException;

public class ProductSelectionState extends State {
    public ProductSelectionState(VendingMachine vendingMachine) {
        super(vendingMachine);
    }

    @Override
    public void insertCash(double amount) throws OperationNotApplicableException {
        vendingMachine.setState(vendingMachine.getHasMoneyState());
        vendingMachine.setAvailableBalance(vendingMachine.getAvailableBalance() + amount);
    }

    @Override
    public void moveToProductSelect() throws OperationNotApplicableException {
        // no-op
    }

    @Override
    public Item makeProductPurchase(String id) throws Exception {
        Item item = null;
        try {
            item = vendingMachine.dispenseProduct(id, 1);
        } catch (InsufficientBalanceException e) {
            vendingMachine.setState(vendingMachine.getHasMoneyState());
            throw e;
        }
        return item;
    }
}
