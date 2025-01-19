package org.sundeep.atm.machine;

import org.sundeep.atm.exceptions.InvalidCredentialException;
import org.sundeep.atm.machine.processor.OperationProcessor;
import org.sundeep.atm.machine.processor.WithdrawMoneyDto;
import org.sundeep.atm.machine.state.AtmState;
import org.sundeep.atm.machine.state.HasCardAtmState;
import org.sundeep.atm.machine.state.IdleAtmState;
import org.sundeep.atm.machine.state.OperationSelectionAtmState;
import org.sundeep.atm.models.Card;

import java.util.Map;
import java.util.TreeMap;

class MoneyDispenser {
    private int amount;
    private int count;
    private MoneyDispenser next;

    MoneyDispenser(int amount, int count) {
        this.amount = amount;
        this.count = count;
    }

    void setNext(MoneyDispenser next) {
        this.next = next;
    }

    Map<Integer, Integer> dispenseCash(double amountToDispense) {
        Map<Integer, Integer> counts = new TreeMap<>();

        if (amountToDispense <= 0) {
            return counts;
        }

        int countNeeded = (int)(amountToDispense / amount);
        int countToDispense = Math.min(countNeeded, count);
        count -= countToDispense;

        counts.put(amount, countToDispense);

        if (next != null) {
            Map<Integer, Integer> sub = next.dispenseCash(amountToDispense - amount * countToDispense);
            counts.putAll(sub);
        }
        return counts;
    }
}

public class Atm {
    private final OperationProcessor operationProcessor = new OperationProcessor();

    private final IdleAtmState idleAtmState = new IdleAtmState(this);
    private final HasCardAtmState hasCardAtmState = new HasCardAtmState(this);
    private final OperationSelectionAtmState operationSelectionAtmState = new OperationSelectionAtmState(this, operationProcessor);

    private AtmState curState;

    private Card card;

    private MoneyDispenser moneyDispenser;

    public Atm(TreeMap<Integer, Integer> amounts) {
        this.curState = idleAtmState;
        buildMoneyDispenser(amounts);
    }

    private void buildMoneyDispenser(TreeMap<Integer, Integer> amounts) {
        int i = 0;
        MoneyDispenser prev = null;
        for (Map.Entry<Integer, Integer> entry: amounts.descendingMap().entrySet()) {
            MoneyDispenser cur = new MoneyDispenser(entry.getKey(), entry.getValue());
            if (i > 0) {
                prev.setNext(cur);
            } else {
                this.moneyDispenser = cur;
            }
            prev = cur;
            i++;
        }
    }

    public void insertCard(Card card) {
        this.curState.insertCard(card);
        this.card = card;
    }

    public void authenticatePin(String pin) {
        try {
            this.curState.authenticatePin(pin);
        } catch (InvalidCredentialException e) {
            System.out.println("Invalid credentials entered. Please try again. " + e.getMessage());
            this.card = null;
        }
    }

    public WithdrawMoneyDto dispenseCash(double amount) {
        Map<Integer, Integer> counts = this.moneyDispenser.dispenseCash(amount);
        double total = 0;
        for (Map.Entry<Integer, Integer> entry: counts.entrySet()) {
            total += entry.getKey() * entry.getValue();
        }
        return new WithdrawMoneyDto(total, counts);
    }

    public void performOperation() {
        this.curState.performOperation();
    }

    public IdleAtmState getIdleAtmState() {
        return idleAtmState;
    }

    public HasCardAtmState hasCardAtmState() {
        return hasCardAtmState;
    }

    public OperationSelectionAtmState operationSelectionAtmState() {
        return operationSelectionAtmState;
    }

    public AtmState getCurState() {
        return curState;
    }

    public void setCurState(AtmState curState) {
        this.curState = curState;
    }

    public void resetCard() {
        card = null;
    }

    public Card getCard() {
        return card;
    }
}
