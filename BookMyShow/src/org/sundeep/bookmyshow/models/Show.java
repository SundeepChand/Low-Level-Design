package org.sundeep.bookmyshow.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

@Getter
@AllArgsConstructor
public class Show {
    private int id;
    private String name;
    private Duration duration;
}
