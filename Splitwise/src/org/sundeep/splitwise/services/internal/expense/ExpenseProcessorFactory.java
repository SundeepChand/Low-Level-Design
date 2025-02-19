package org.sundeep.splitwise.services.internal.expense;

import org.sundeep.splitwise.dto.AddExpenseDto;
import org.sundeep.splitwise.models.Expense;
import org.sundeep.splitwise.services.GroupService;
import org.sundeep.splitwise.services.UserService;

import java.util.List;
import java.util.Map;

public class ExpenseProcessorFactory {
    private final EqualSplitExpenseProcessor equalSplitExpenseProcessor;

    public ExpenseProcessorFactory(UserService userService, GroupService groupService, Map<String, List<Expense>> groupIdToExpensesMap) {
        this.equalSplitExpenseProcessor = new EqualSplitExpenseProcessor(userService, groupService, groupIdToExpensesMap);
    }

    public ExpenseProcessor getExpenseValidator(AddExpenseDto data) throws IllegalArgumentException {
        switch (data.getSplitType()) {
            case SPLIT_TYPE_EQUAL:
                return equalSplitExpenseProcessor;
            case SPLIT_TYPE_PERCENTAGE:
            case SPLIT_TYPE_UNEQUAL:
            default:
                throw new IllegalArgumentException("unsupported split type passed " + data.getSplitType());
        }
    }
}
