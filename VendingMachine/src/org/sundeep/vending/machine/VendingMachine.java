package org.sundeep.vending.machine;

import org.sundeep.vending.machine.state.HasMoneyState;
import org.sundeep.vending.machine.state.IdleState;
import org.sundeep.vending.machine.state.ProductSelectionState;
import org.sundeep.vending.machine.state.State;
import org.sundeep.vending.machine.state.exceptions.InsufficientBalanceException;
import org.sundeep.vending.machine.state.exceptions.OperationNotApplicableException;
import org.sundeep.vending.machine.state.exceptions.ProductNotAvailableException;

import java.util.HashMap;
import java.util.Map;

public class VendingMachine {
    private final State idleState = new IdleState(this);
    private final State hasMoneyState = new HasMoneyState(this);
    private final State productSelectionState = new ProductSelectionState(this);

    private State curState;

    private double availableBalance;

    private final Map<String, Item> availableProducts;
    private final Map<String, Integer> productQuantities;

    public VendingMachine() {
        curState = idleState;
        availableBalance = 0;
        availableProducts = new HashMap<>();
        productQuantities = new HashMap<>();
    }

    public void addProduct(Item item, int quantity) {
        availableProducts.put(item.getId(), item);
        productQuantities.put(item.getId(), productQuantities.getOrDefault(item.getId(), 0) + quantity);
    }

    public Item dispenseProduct(String id, int quantity) throws Exception {
        int availableQuantity = productQuantities.getOrDefault(id, 0);
        if (quantity > availableQuantity) {
            throw new ProductNotAvailableException("Required quantity of " + id + " product not available");
        }
        Item item =  availableProducts.get(id);
        if (availableBalance < quantity * item.getPrice()) {
            throw new InsufficientBalanceException("Not enough balance available to purchase product: " + id + ", availableBal: " + availableBalance);
        }
        availableBalance -= quantity * item.getPrice();
        productQuantities.put(id, availableQuantity - quantity);
        return item;
    }

    public void insertCash(double amount) {
        try {
            curState.insertCash(amount);
        } catch (OperationNotApplicableException e) {
            System.out.println(e.getMessage());
        }
    }

    public void startProductSelection() {
        try {
            curState.moveToProductSelect();
        } catch (OperationNotApplicableException e) {
            System.out.println(e.getMessage());
        }
    }

    public void purchaseProduct(String id) {
        try {
            Item item = curState.makeProductPurchase(id);
            System.out.println("Purchased one unit of: " + item.getId() + ", name: " + item.getName());
        } catch (ProductNotAvailableException e) {
            System.out.println("Desired item not available: " + id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void checkOut() {
        curState.checkout();
    }

    public void processRefundBalance() {
        System.out.println("Remaining amount returned: " + availableBalance);
        availableBalance = 0;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double finalBalance) {
        this.availableBalance = finalBalance;
    }

    public State getCurState() {
        return this.curState;
    }

    public void setState(State newState) {
        this.curState = newState;
    }

    public State getIdleState() {
        return this.idleState;
    }

    public State getHasMoneyState() {
        return this.hasMoneyState;
    }

    public State getProductSelectionState() {
        return productSelectionState;
    }
}
