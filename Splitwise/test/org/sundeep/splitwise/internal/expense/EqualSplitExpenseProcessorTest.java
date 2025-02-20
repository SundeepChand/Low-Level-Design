package org.sundeep.splitwise.internal.expense;

import org.junit.jupiter.api.Test;
import org.sundeep.splitwise.dto.AddExpenseDto;
import org.sundeep.splitwise.models.*;
import org.sundeep.splitwise.services.GroupService;
import org.sundeep.splitwise.services.UserService;
import org.sundeep.splitwise.services.internal.expense.EqualSplitExpenseProcessor;

import java.util.*;

import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EqualSplitExpenseProcessorTest {

    @Test
    void validateExpenseInputShouldNotThrowExceptionForCorrectInput() {
        UserService mockUserService = mock(UserService.class);
        GroupService mockGroupService = mock(GroupService.class);

        Map<String, List<Expense>> groupIdToExpensesMap = new HashMap<>();

        EqualSplitExpenseProcessor processor = new EqualSplitExpenseProcessor(mockUserService, mockGroupService, groupIdToExpensesMap);

        assertDoesNotThrow(() -> {
            processor.validateExpenseInput(new AddExpenseDto(
                    "My-Expense",
                    "user-id-1",
                    "group-id-1",
                    new Money(100, "INR"),
                    SplitType.SPLIT_TYPE_EQUAL,
                    null
            ));
        });
    }

    @Test
    void validateExpenseInputShouldThrowExceptionWhenSplitTypeIsNotEqual() {
        UserService mockUserService = mock(UserService.class);
        GroupService mockGroupService = mock(GroupService.class);

        Map<String, List<Expense>> groupIdToExpensesMap = new HashMap<>();

        EqualSplitExpenseProcessor processor = new EqualSplitExpenseProcessor(mockUserService, mockGroupService, groupIdToExpensesMap);

        assertThrows(IllegalArgumentException.class, () -> {
            processor.validateExpenseInput(new AddExpenseDto(
                    "My-Expense",
                    "user-id-1",
                    "group-id-1",
                    new Money(100, "INR"),
                    SplitType.SPLIT_TYPE_PERCENTAGE,
                    null
            ));
        });
    }

    @Test
    void addExpenseShouldSuccessfullyAddExpensesForGivenInput() {
        UserService mockUserService = mock(UserService.class);
        GroupService mockGroupService = mock(GroupService.class);

        Map<String, List<Expense>> groupIdToExpensesMap = new HashMap<>();

        EqualSplitExpenseProcessor processor = new EqualSplitExpenseProcessor(mockUserService, mockGroupService, groupIdToExpensesMap);

        User user1 = new User("User-1"), user2 = new User("User-2");
        List<User> groupOneUsers = new ArrayList<>();
        groupOneUsers.add(user1);
        groupOneUsers.add(user2);

        Group group1 = new Group("MyGroup", groupOneUsers);

        when(mockGroupService.getById(group1.getId())).thenReturn(group1);
        when(mockUserService.getById(user2.getId())).thenReturn(user2);

        SplitType equalSplitType = SplitType.SPLIT_TYPE_EQUAL;
        processor.addExpense(new AddExpenseDto(
                "Expense 1",
                user2.getId(),
                group1.getId(),
                new Money(100, "INR"),
                equalSplitType,
                null
        ));

        assertEquals(1, groupIdToExpensesMap.size());
        assertEquals(1, groupIdToExpensesMap.get(group1.getId()).size());

        Expense addedExpense = groupIdToExpensesMap.get(group1.getId()).getFirst();
        // Set ID to empty for now since we don't have control over what ID gets assigned to the object.
        addedExpense.setId("");
        addedExpense.setSplitDetails(null);
        Expense expectedExpense = new Expense("Expense 1", new Money(100, "INR"), equalSplitType, user2);
        // Set ID to empty for now since we don't have control over what ID gets assigned to the object.
        expectedExpense.setId("");
        expectedExpense.setSplitDetails(null);

        assertEquals(addedExpense, expectedExpense);

        System.out.println(groupIdToExpensesMap);
        System.out.println(user1.getBalanceSheet());
        System.out.println(user2.getBalanceSheet());
    }
}