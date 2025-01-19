package org.sundeep.atm;

import org.sundeep.atm.machine.Atm;
import org.sundeep.atm.models.BankAccount;
import org.sundeep.atm.models.Card;

import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        TreeMap<Integer, Integer> moneyFreq = new TreeMap<>();
        moneyFreq.put(500, 4);
        moneyFreq.put(200, 7);
        moneyFreq.put(100, 2);
        moneyFreq.put(50, 2);
        moneyFreq.put(10, 10);
        moneyFreq.put(1, 100);

        Atm atm = new Atm(moneyFreq);

        Card card = new Card("123456", 123, new Date(2025, Calendar.DECEMBER, 31), "ABC");
        BankAccount bankAccount = new BankAccount(
                "123",
                card,
                10000);

        atm.insertCard(card);
        atm.authenticatePin("ABC");
        atm.performOperation();
        // atm.performOperation();

        atm.insertCard(card);
        atm.authenticatePin("ABC");
        atm.performOperation();

        atm.insertCard(card);
        atm.authenticatePin("ABC");
        atm.performOperation();

        atm.insertCard(card);
        atm.authenticatePin("123");
    }
}
