package com.kotchakorn.weatherAPI.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WeatherDto {

    private Integer id;

    private String date;

    private Float lat;
    private Float lon;
    private String city;
    private String state;

    private List<Double> temperatures =new ArrayList<>();
}
