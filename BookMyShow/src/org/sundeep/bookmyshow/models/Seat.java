package org.sundeep.bookmyshow.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Seat {
    private int id;
    private double price;
    private SeatCategory category;
}
