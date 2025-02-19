package org.sundeep.splitwise.models;

public class Money {
    double amount;
    private String currency;

    public Money() {
        this.amount = 0;
        this.currency = "INR";
    }

    public Money(String currency) {
        this.amount = 0;
        this.currency = currency;
    }

    public Money(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return currency + " " + String.format("%.2f", amount);
    }
}
