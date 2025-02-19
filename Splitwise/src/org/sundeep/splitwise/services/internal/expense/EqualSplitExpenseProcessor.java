package org.sundeep.splitwise.services.internal.expense;

import org.sundeep.splitwise.dto.AddExpenseDto;
import org.sundeep.splitwise.models.*;
import org.sundeep.splitwise.services.GroupService;
import org.sundeep.splitwise.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EqualSplitExpenseProcessor extends ExpenseProcessor {
    public EqualSplitExpenseProcessor(UserService userService, GroupService groupService, Map<String, List<Expense>> groupIdToExpensesMap) {
        super(userService, groupService, groupIdToExpensesMap);
    }

    @Override
    public void validateExpenseInput(AddExpenseDto data) throws IllegalArgumentException {
        // Basic checks that the field names are present
        if (data.getSplitType() != SplitType.SPLIT_TYPE_EQUAL) {
            throw new IllegalArgumentException("Invalid split type " + data.getSplitType() + " for equal expense split validator");
        }
        if (data.getSplits() != null) {
            throw new IllegalArgumentException("Individual splits must be nil for the API");
        }
        if (data.getExpenseName().isBlank() || data.getGroupId().isBlank() || data.getFromUserId().isBlank()) {
            throw new IllegalArgumentException("Empty parameters passed for equal split");
        }
        if (data.getAmountSpent().getAmount() <= 0) {
            throw new IllegalArgumentException("Invalid amount passed for equal split");
        }
    }

    @Override
    public void addExpense(AddExpenseDto addExpenseDto) {
        Group fetchedGroup = groupService.getById(addExpenseDto.getGroupId());
        if (fetchedGroup == null) {
            return;
        }

        User paidByUser = userService.getById(addExpenseDto.getFromUserId());
        if (paidByUser == null) {
            return;
        }

        // Calculate the balances for each of the users and update in their balance sheet.
        List<User> allUsers = fetchedGroup.getUsers();
        int nUsers = allUsers.size();
        double share = addExpenseDto.getAmountSpent().getAmount() / nUsers;

        Expense curExpense = new Expense(
                addExpenseDto.getExpenseName(),
                addExpenseDto.getAmountSpent(),
                SplitType.SPLIT_TYPE_EQUAL,
                paidByUser
        );

        for (User user : allUsers) {
            if (user.getId().equals(addExpenseDto.getFromUserId())) {
                continue;
            }
            Money shareAmount = new Money(share, "INR");

            curExpense.addSplit(new Split(user, paidByUser, shareAmount));

            Balance paidForUserBalance = new Balance(shareAmount, true);

            // We just naively keep track of who owes how much to whom.
            // This does not do any simplification in the number of transactions.
            user.getBalanceSheet().addNewBalanceForUser(paidByUser, paidForUserBalance);
        }

        groupIdToExpensesMap.put(
                addExpenseDto.getGroupId(),
                groupIdToExpensesMap.getOrDefault(addExpenseDto.getGroupId(), new ArrayList<>())
        );
        groupIdToExpensesMap.get(addExpenseDto.getGroupId()).add(curExpense);
    }
}
