package org.sundeep.bookmyshow.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Venue {
    private int id;
    private String address;
    private City city;
    private List<Hall> halls;
    private List<Slot> slots;

    public boolean hasUpcomingShow(String showName) {
        for (Slot slot: slots) {
            if (slot.getShow().getName().equals(showName)) {
                return true;
            }
        }
        return false;
    }
}
