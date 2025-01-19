package org.sundeep.atm.models;

import org.sundeep.vending.machine.state.exceptions.InsufficientBalanceException;

public class BankAccount {
    private String accountNo;
    private double balance;

    public BankAccount(String accountNo, Card card, double initialBalance) {
        this.accountNo = accountNo;
        this.balance = initialBalance;
        card.setAssociatedBankAccount(this);
    }

    public void deductBalance(double amount) throws InsufficientBalanceException {
        if (amount > balance) {
            throw new InsufficientBalanceException("Can't deduct INR: " + amount + ". Insufficient funds");
        }
        this.balance -= amount;
    }

    public void addBalance(double amount) {
        this.balance += amount;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public double getBalance() {
        return this.balance;
    }
}
