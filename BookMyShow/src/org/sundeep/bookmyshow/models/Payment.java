package org.sundeep.bookmyshow.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Payment {
    private int id;
    private double price;
    private PaymentStatus status;

    public void setPaymentStatus(PaymentStatus newStatus) {
        status = newStatus;
    }
}
