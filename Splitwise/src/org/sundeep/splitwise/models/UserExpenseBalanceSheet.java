package org.sundeep.splitwise.models;

import java.util.HashMap;
import java.util.Map;

public class UserExpenseBalanceSheet {
    private final User fromUser;
    private final Map<User, Balance> unsettledBalances;
    private final Balance totalBalance;

    public UserExpenseBalanceSheet(User user) {
        this.fromUser = user;
        unsettledBalances = new HashMap<>();
        totalBalance = new Balance(new Money("INR"), false);
    }

    public Balance getTotalBalance() {
        return totalBalance;
    }

    public void addNewBalanceForUser(User user, Balance newBalance) {
        totalBalance.addBalance(newBalance);
        if (unsettledBalances.get(user) == null) {
            unsettledBalances.put(user, newBalance);
            return;
        }
        Balance existingBalance = unsettledBalances.get(user);
        existingBalance.addBalance(newBalance);
    }

    public void display() {
        if (!unsettledBalances.isEmpty()) {
            unsettledBalances.forEach((user, balance) -> {
                System.out.println(
                        fromUser.getName() + " " + balance.toString() + (balance.isOwe() ? " to " : " from ") + user.getName()
                );
            });
        } else {
            System.out.println("All settled");
        }
        System.out.println("Total balance: " + totalBalance);
        System.out.println("-------\n");
    }
}
