package org.sundeep.atm.machine.processor;

import org.sundeep.atm.machine.Atm;
import org.sundeep.atm.models.BankAccount;
import org.sundeep.vending.machine.state.exceptions.InsufficientBalanceException;

import java.util.Map;

public class WithdrawMoneyOperation implements Operation {
    private final BankAccount account;
    private final double amount;
    private final Atm atm;

    public WithdrawMoneyOperation(Atm atm, BankAccount account, double amount) {
        this.atm = atm;
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void perform() {
        try {
            this.account.deductBalance(amount);
            WithdrawMoneyDto withdrawResult = atm.dispenseCash(amount);
            // Add the (amount requested - amount in withdrawResult) to the bank account, as the diff denotes the
            // amount that could not be fulfilled by the atm.
            this.account.addBalance(this.amount - withdrawResult.getAmountWithdrawn());
            if (withdrawResult.getAmountWithdrawn() < this.amount) {
                System.out.println("Could only dispense INR " + withdrawResult.getAmountWithdrawn());
            } else {
                System.out.println("Successfully dispensed requested amount of INR " + withdrawResult.getAmountWithdrawn());
            }
            if (withdrawResult.getMoneyFreq().isEmpty()) {
                return;
            }
            System.out.println("Frequency of money withdrawn:");
            for (Map.Entry<Integer, Integer> entry : withdrawResult.getMoneyFreq().entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println();
        } catch (InsufficientBalanceException e) {
            System.out.println(e.getMessage());
        }
    }
}
