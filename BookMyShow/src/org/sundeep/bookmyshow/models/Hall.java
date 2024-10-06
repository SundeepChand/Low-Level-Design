package org.sundeep.bookmyshow.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Hall {
    private int id;
    private List<Seat> allSeats;
}
