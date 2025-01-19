package org.sundeep.atm.machine.processor;

import java.util.Map;

public class WithdrawMoneyDto {
    private double amountWithdrawn;
    private Map<Integer, Integer> moneyFreq;

    public WithdrawMoneyDto(double amountWithdrawn, Map<Integer, Integer> moneyFreq) {
        this.amountWithdrawn = amountWithdrawn;
        this.moneyFreq = moneyFreq;
    }

    public double getAmountWithdrawn() {
        return amountWithdrawn;
    }

    public Map<Integer, Integer> getMoneyFreq() {
        return moneyFreq;
    }
}
