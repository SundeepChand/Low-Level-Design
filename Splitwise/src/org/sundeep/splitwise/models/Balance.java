package org.sundeep.splitwise.models;

public class Balance {
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
