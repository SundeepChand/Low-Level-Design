package org.sundeep.atm.models;

import java.util.Date;

public class Card {
    private String number;
    private int cvv;
    private Date cardExpiry;
    private String pin;
    private BankAccount associatedBankAccount;

    public Card(String number, int cvv, Date cardExpiry, String pin) {
        this.number = number;
        this.cardExpiry = cardExpiry;
        this.cvv = cvv;
        this.pin = pin;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public Date getCardExpiry() {
        return cardExpiry;
    }

    public String getPin() {
        return pin;
    }

    void setAssociatedBankAccount(BankAccount bankAccount) {
        this.associatedBankAccount = bankAccount;
    }

    public BankAccount getAssociatedBankAccount() {
        return associatedBankAccount;
    }

    public void setCardExpiry(Date cardExpiry) {
        this.cardExpiry = cardExpiry;
    }
}
