package com.kotchakorn.weatherAPI.repository;

import com.kotchakorn.weatherAPI.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather, Integer> {
    List<Weather> findAllByOrderByIdAsc();
    List<Weather> findAllByOrderByDateAsc();
    List<Weather> findAllByOrderByDateDesc();
    List<Weather> findAllByDate(Date date);
    List<Weather> findAllByCityIgnoreCase(String city);
}
