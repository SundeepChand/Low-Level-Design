package org.sundeep.bookmyshow.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
public class Slot {
    private int id;
    private Show show;
    private Hall hall;
    private ZonedDateTime startTime;
    private List<Seat> availableSeats;
    // Seats which are currently being reserved, but the reservation is not
    // complete because of some dependency, eg. Pending payment
    private List<Seat> lockedSeats;

    public Slot(int id, Show show, Hall hall, ZonedDateTime startTime) {
        this.id = id;
        this.show = show;
        this.hall = hall;
        this.startTime = startTime;
        this.availableSeats = new LinkedList<>();
        for (Seat seat: hall.getAllSeats()) {
            this.availableSeats.add(seat);
        }
        this.lockedSeats = new ArrayList<>();
    }

    public void lockSeats(List<Seat> seats) {
        for (Seat seat: seats) {
            availableSeats.remove(seat);
            lockedSeats.add(seat);
        }
    }

    public void revertLockedSeats(List<Seat> seats) {
        for (Seat seat: seats) {
            lockedSeats.remove(seat);
            availableSeats.add(seat);
        }
    }

    public void confirmLockedSeats(List<Seat> seats) {
        for (Seat seat: seats) {
            lockedSeats.remove(seat);
        }
    }
}
