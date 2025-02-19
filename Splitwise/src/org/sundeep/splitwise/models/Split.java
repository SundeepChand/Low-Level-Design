package org.sundeep.splitwise.models;

import java.util.UUID;

public class Split {
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
