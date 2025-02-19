package org.sundeep.splitwise.dto;

import org.sundeep.splitwise.models.Money;
import org.sundeep.splitwise.models.Split;
import org.sundeep.splitwise.models.SplitType;

import java.util.List;

public class AddExpenseDto {
    private final String expenseName;
    private final String fromUserId;
    private final String groupId;
    private final Money amountSpent;
    private final SplitType splitType;
    // optional when splitType is EQUAL. Mandatory for all other cases.
    private final List<Split> splits;

    public String getExpenseName() {
        return expenseName;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public String getGroupId() {
        return groupId;
    }

    public Money getAmountSpent() {
        return amountSpent;
    }

    public SplitType getSplitType() {
        return splitType;
    }

    public List<Split> getSplits() {
        return splits;
    }

    public AddExpenseDto(String expenseName, String fromUserId, String groupId, Money amountSpent, SplitType splitType, List<Split> splits) {
        this.expenseName = expenseName;
        this.fromUserId = fromUserId;
        this.groupId = groupId;
        this.amountSpent = amountSpent;
        this.splitType = splitType;
        this.splits = splits;
    }
}
