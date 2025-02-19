package org.sundeep.splitwise.services.internal.expense;

import org.sundeep.splitwise.dto.AddExpenseDto;
import org.sundeep.splitwise.models.Expense;
import org.sundeep.splitwise.services.GroupService;
import org.sundeep.splitwise.services.UserService;

import java.util.List;
import java.util.Map;

public abstract class ExpenseProcessor {
    protected final UserService userService;
    protected final GroupService groupService;
    protected final Map<String, List<Expense>> groupIdToExpensesMap;

    public ExpenseProcessor(UserService userService, GroupService groupService, Map<String, List<Expense>> groupIdToExpensesMap) {
        this.groupService = groupService;
        this.userService = userService;
        this.groupIdToExpensesMap = groupIdToExpensesMap;
    }

    abstract public void validateExpenseInput(AddExpenseDto data) throws IllegalArgumentException;

    abstract public void addExpense(AddExpenseDto addExpenseDto);
}
