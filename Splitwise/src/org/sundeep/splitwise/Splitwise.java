package org.sundeep.splitwise;

import java.util.*;

class Money {
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

class Group {
    private String id;
    private String name;
    private List<User> users;

    public Group(String name, List<User> users) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }
}

class Balance {
    private final Money amount;
    // isOwe is true for cases when user needs to pay money back.
    // and for cases where user will receive money, isOwe is false.
    private boolean isOwe;

    public Balance(Money amount, boolean isOwe) {
        this.amount = amount;
        this.isOwe = isOwe;
    }

    public Money getAmount() {
        return amount;
    }

    public boolean isOwe() {
        return isOwe;
    }

    public void addBalance(Balance additionalBalance) {
        boolean curIsOwe = isOwe;
        boolean otherBalIsOwe = additionalBalance.isOwe();
        if ((curIsOwe && otherBalIsOwe) || (!curIsOwe && !otherBalIsOwe)) {
            amount.setAmount(amount.getAmount() + additionalBalance.getAmount().getAmount());
            return;
        }
        if (otherBalIsOwe) {
            if (additionalBalance.getAmount().getAmount() > amount.getAmount()) {
                isOwe = true;
            }
            amount.setAmount(Math.abs(amount.getAmount() - additionalBalance.getAmount().getAmount()));
        } else {
            if (additionalBalance.getAmount().getAmount() > amount.getAmount()) {
                isOwe = false;
            }
            amount.setAmount(Math.abs(amount.getAmount() - additionalBalance.getAmount().getAmount()));
        }
    }

    @Override
    public String toString() {
        return (isOwe ? "owes " : "gets ") + amount.toString();
    }
}

class UserExpenseBalanceSheet {
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

class User {
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

enum SplitType {
    SPLIT_TYPE_EQUAL,
    SPLIT_TYPE_UNEQUAL,
    SPLIT_TYPE_PERCENTAGE
}

class Split {
    private final String id;
    private User fromUser;
    private User toUser;
    private Money amount;

    public Split(User fromUser, User toUser, Money amount) {
        this.id = UUID.randomUUID().toString();
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }
}

class Expense {
    private String id;
    private String name;
    private Money totalAmount;
    private SplitType splitType;
    private List<Split> splitDetails;
    private User paidBy;

    public Expense(String name, Money totalAmount, SplitType splitType, User paidBy) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.totalAmount = totalAmount;
        this.splitType = splitType;
        this.splitDetails = new ArrayList<>();
        this.paidBy = paidBy;
    }

    public void addSplit(Split split) {
        splitDetails.add(split);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
    }

    public SplitType getSplitType() {
        return splitType;
    }

    public void setSplitType(SplitType splitType) {
        this.splitType = splitType;
    }

    public List<Split> getSplitDetails() {
        return splitDetails;
    }

    public void setSplitDetails(List<Split> splitDetails) {
        this.splitDetails = splitDetails;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(User paidBy) {
        this.paidBy = paidBy;
    }

    public void display() {
        for (Split split: splitDetails) {
            System.out.println(split.getFromUser().getName() + " -> " + split.getToUser().getName() + ": " + split.getAmount());
        }
        System.out.println("Total amount: " + totalAmount);
    }
}

class UserService {
    private final List<User> users;

    public UserService() {
        users = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public User getById(String userId) {
        for (User u: users) {
            if (u.getId().equals(userId)) {
                return u;
            }
        }
        return null;
    }
}

class GroupService {
    private final UserService userService;

    private final Map<String, Group> groups;

    public GroupService(UserService userService) {
        groups = new HashMap<>();
        this.userService = userService;
    }

    public void createGroup(Group group) {
        groups.put(group.getId(), group);
    }

    public void addUserToGroup(String groupId, User user) throws IllegalArgumentException {
        if (groups.get(groupId) == null) {
            throw new IllegalArgumentException("group with groupId: " + groupId + " does not exist");
        }
        groups.get(groupId).addUser(user);
    }

    public Group getById(String groupId) {
        return groups.get(groupId);
    }
}

class BalanceSheetService {
    public void updateBalanceSheet() {

    }
}

class ExpenseService {
    private final UserService userService;
    private final GroupService groupService;
    private final BalanceSheetService balanceSheetService;

    private final Map<String, List<Expense>> groupIdToExpensesMap;

    public ExpenseService(BalanceSheetService balanceSheetService, UserService userService, GroupService groupService) {
        this.balanceSheetService = balanceSheetService;
        this.groupService = groupService;
        this.userService = userService;
        this.groupIdToExpensesMap = new HashMap<>();
    }

    public void addExpenseEqualSplit(String name, String fromUserId, String groupId, Money amountSpent) {
        Group fetchedGroup = groupService.getById(groupId);
        if (fetchedGroup == null) {
            return;
        }

        User paidByUser = userService.getById(fromUserId);
        if (paidByUser == null) {
            return;
        }

        // Calculate the balances for each of the users and update in their balance sheet.
        List<User> allUsers = fetchedGroup.getUsers();
        int nUsers = allUsers.size();
        double share = amountSpent.getAmount() / nUsers;

        Expense curExpense = new Expense(name, amountSpent, SplitType.SPLIT_TYPE_EQUAL, paidByUser);

        for (User user: allUsers) {
            if (user.getId().equals(fromUserId)) {
                continue;
            }
            Money shareAmount = new Money(share, "INR");

            curExpense.addSplit(new Split(user, paidByUser, shareAmount));

            Balance paidForUserBalance = new Balance(shareAmount, true);

            // We just naively keep track of who owes how much to whom.
            // This does not do any simplification in the number of transactions.
            user.getBalanceSheet().addNewBalanceForUser(paidByUser, paidForUserBalance);
        }

        groupIdToExpensesMap.put(groupId, groupIdToExpensesMap.getOrDefault(groupId, new ArrayList<>()));
        groupIdToExpensesMap.get(groupId).add(curExpense);
    }

    public void printExpensesForGroup(String groupId) {
        List<Expense> expenses = groupIdToExpensesMap.get(groupId);
        if (expenses == null) {
            return;
        }
        for (Expense expense: expenses) {
            System.out.println("Expense name: " + expense.getName());
            expense.display();
            System.out.println("-------\n");
        }
    }
}

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

        expenseService.addExpenseEqualSplit(
                "Breakfast",
                user1.getId(),
                group1.getId(),
                new Money(90, "INR")
        );
        expenseService.addExpenseEqualSplit(
                "Lunch",
                user2.getId(),
                group1.getId(),
                new Money(210, "INR")
        );

        expenseService.printExpensesForGroup(group1.getId());
        user1.getBalanceSheet().display();
        user2.getBalanceSheet().display();
        user3.getBalanceSheet().display();
    }
}
