package org.sundeep.bookmyshow.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.sundeep.bookmyshow.models.City;
import org.sundeep.bookmyshow.models.Show;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class ShowService {
    private final Map<City, List<Show>> cityToShowsMap;
    private List<Show> allShows;

    public ShowService() {
        cityToShowsMap = new HashMap<>();
        allShows = new ArrayList<>();
    }

    public void addShow(City city, Show show) {
        if (cityToShowsMap.getOrDefault(city, null) == null) {
            cityToShowsMap.put(city, new ArrayList<>());
        }
        cityToShowsMap.get(city).add(show);
    }
}
