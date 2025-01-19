package org.sundeep.atm.machine.processor;

import org.sundeep.atm.models.BankAccount;

public class CheckBalanceOperation implements Operation {
    private final BankAccount account;

    public CheckBalanceOperation(BankAccount account) {
        this.account = account;
    }

    @Override
    public void perform() {
        System.out.println("Balance in account " + account.getAccountNo() + " = " + account.getBalance());
    }
}
