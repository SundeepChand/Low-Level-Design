package org.sundeep.bookmyshow.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Booking {
    private int id;
    private Slot slot;
    private List<Seat> seats;
    @Setter
    private Payment payment;
    @Setter
    private BookingStatus bookingStatus;

    @Override
    public String toString() {
        return id + " " + slot.getShow() + " " + slot.getHall() + " " + seats + " " +
                payment.getPrice() + " " + payment.getStatus() + " " + bookingStatus;
    }
}
