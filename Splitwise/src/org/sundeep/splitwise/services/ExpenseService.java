package org.sundeep.splitwise.services;

import org.sundeep.splitwise.dto.AddExpenseDto;
import org.sundeep.splitwise.services.internal.expense.ExpenseProcessor;
import org.sundeep.splitwise.models.Expense;
import org.sundeep.splitwise.services.internal.expense.ExpenseProcessorFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseService {

    private final Map<String, List<Expense>> groupIdToExpensesMap;

    private final ExpenseProcessorFactory expenseProcessorFactory;

    public ExpenseService(BalanceSheetService balanceSheetService, UserService userService, GroupService groupService) {
        this.groupIdToExpensesMap = new HashMap<>();
        this.expenseProcessorFactory = new ExpenseProcessorFactory(userService, groupService, groupIdToExpensesMap);
    }

    public void addExpense(AddExpenseDto addExpenseDto) {
        ExpenseProcessor expenseProcessor = expenseProcessorFactory.getExpenseValidator(addExpenseDto);
        expenseProcessor.validateExpenseInput(addExpenseDto);
        expenseProcessor.addExpense(addExpenseDto);
    }

    public void printExpensesForGroup(String groupId) {
        List<Expense> expenses = groupIdToExpensesMap.get(groupId);
        if (expenses == null) {
            return;
        }
        for (Expense expense : expenses) {
            System.out.println("Expense name: " + expense.getName());
            expense.display();
            System.out.println("-------\n");
        }
    }
}
