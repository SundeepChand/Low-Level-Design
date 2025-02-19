package org.sundeep.splitwise.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Expense {
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
