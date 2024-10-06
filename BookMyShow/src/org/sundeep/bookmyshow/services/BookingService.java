package org.sundeep.bookmyshow.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.sundeep.bookmyshow.models.*;

import java.awt.print.Book;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class BookingService {
    private final Map<Integer, Booking> allBookings;

    public BookingService() {
        allBookings = new HashMap<>();
    }

    private int generateId() {
        return (int)(System.currentTimeMillis() / 1000);
    }

    public Booking createBooking(Slot slot, List<Seat> seats) {
        double totalPrice = 0;
        for (Seat seat: seats) {
            totalPrice += seat.getPrice();
        }
        Booking booking = new Booking(
                this.generateId(),
                slot,
                seats,
                new Payment(this.generateId(), totalPrice, PaymentStatus.PENDING),
                BookingStatus.PENDING_PAYMENT
        );

        // Lock the seats and create a thread to check for payment status.
        // If the payment succeeds within a given timeout then, mark the seats as booked, else release them.
        slot.lockSeats(seats);

        allBookings.put(booking.getId(), booking);

        return booking;
    }

    private BookingStatus convertPaymentStatusToBookingStatus(PaymentStatus status) {
        return switch (status) {
            case PaymentStatus.FAILURE -> BookingStatus.FAILED;
            case PaymentStatus.SUCCESS -> BookingStatus.CONFIRMED;
            default -> BookingStatus.PENDING_PAYMENT;
        };
    }

    public Booking updateBookingStatusAfterPayment(int bookingId, Payment payment) throws IllegalArgumentException {
        if (allBookings.getOrDefault(bookingId, null) == null) {
            throw new IllegalArgumentException("Invalid booking id");
        }
        Booking curBooking = allBookings.get(bookingId);
        BookingStatus newBookingStatus = this.convertPaymentStatusToBookingStatus(payment.getStatus());
        switch (newBookingStatus) {
            case CONFIRMED:
                curBooking.getSlot().confirmLockedSeats(curBooking.getSeats());
            default:
                curBooking.getSlot().revertLockedSeats(curBooking.getSeats());
        }
        curBooking.setBookingStatus(newBookingStatus);
        curBooking.setPayment(payment);
        return curBooking;
    }
}
