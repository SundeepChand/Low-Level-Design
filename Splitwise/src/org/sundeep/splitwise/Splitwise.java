package org.sundeep.splitwise;

import org.sundeep.splitwise.dto.AddExpenseDto;
import org.sundeep.splitwise.models.*;
import org.sundeep.splitwise.services.BalanceSheetService;
import org.sundeep.splitwise.services.ExpenseService;
import org.sundeep.splitwise.services.GroupService;
import org.sundeep.splitwise.services.UserService;

import java.util.*;

public class Splitwise {

    public static void main(String[] args) {
        UserService userService = new UserService();
        GroupService groupService = new GroupService(userService);
        BalanceSheetService balanceSheetService = new BalanceSheetService();
        ExpenseService expenseService = new ExpenseService(balanceSheetService, userService, groupService);

        User user1 = new User("user-1");
        User user2 = new User("user-2");
        User user3 = new User("user-3");

        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);

        List<User> groupOneUsers = new ArrayList<>();
        groupOneUsers.add(user1);
        groupOneUsers.add(user2);
        groupOneUsers.add(user3);

        Group group1 = new Group("group-1", groupOneUsers);

        groupService.createGroup(group1);

        expenseService.addExpense(new AddExpenseDto(
                "Breakfast",
                user1.getId(),
                group1.getId(),
                new Money(90, "INR"),
                SplitType.SPLIT_TYPE_EQUAL,
                null
        ));
        expenseService.addExpense(new AddExpenseDto(
                "Lunch",
                user2.getId(),
                group1.getId(),
                new Money(210, "INR"),
                SplitType.SPLIT_TYPE_EQUAL,
                null
        ));

        expenseService.printExpensesForGroup(group1.getId());
        user1.getBalanceSheet().display();
        user2.getBalanceSheet().display();
        user3.getBalanceSheet().display();
    }
}
