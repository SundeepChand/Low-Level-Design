package org.sundeep.bookmyshow.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.sundeep.bookmyshow.models.City;
import org.sundeep.bookmyshow.models.Venue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class VenueService {
    private final Map<City, List<Venue>> venuesByCity;
    private final List<Venue> allVenues;

    public VenueService() {
        venuesByCity = new HashMap<>();
        allVenues = new ArrayList<>();
    }

    public void addVenue(Venue venue) {
        if (venuesByCity.getOrDefault(venue.getCity(), null) == null) {
            venuesByCity.put(venue.getCity(), new ArrayList<>());
        }
        venuesByCity.get(venue.getCity()).add(venue);
        allVenues.add(venue);
    }

    public List<Venue> getAllVenuesInCity(City city) throws IllegalArgumentException {
        if (venuesByCity.getOrDefault(city, null) == null) {
            throw new IllegalArgumentException("Unsupported city " + city.toString());
        }
        return venuesByCity.get(city);
    }

    public List<Venue> getVenuesForShowInCity(String showName, City city) throws IllegalArgumentException {
        if (venuesByCity.getOrDefault(city, null) == null) {
            throw new IllegalArgumentException("Unsupported city " + city.toString());
        }
        return venuesByCity.get(city).stream().filter((Venue venue) -> venue.hasUpcomingShow(showName)).toList();
    }
}