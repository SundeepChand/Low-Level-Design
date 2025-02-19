package org.sundeep.splitwise.models;

import java.util.UUID;

public class User {
    private final String id;
    private final String name;
    private final UserExpenseBalanceSheet balanceSheet;

    public User(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.balanceSheet = new UserExpenseBalanceSheet(this);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UserExpenseBalanceSheet getBalanceSheet() {
        return balanceSheet;
    }

    public void showPendingBalances() {
        this.balanceSheet.display();
    }
}
