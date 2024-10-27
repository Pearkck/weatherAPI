package com.kotchakorn.weatherAPI.controller;

import com.kotchakorn.weatherAPI.model.WeatherDto;
import com.kotchakorn.weatherAPI.service.WeatherService;
import com.kotchakorn.weatherAPI.service.serviceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/weather")
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @PostMapping
    public ResponseEntity<?> createWeather(@RequestBody WeatherDto weatherDto) throws ParseException {
        WeatherDto weather = weatherService.createNewWeather(weatherDto);
        return new ResponseEntity<>(weather, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<?>> getWeathers(@RequestParam(required = false) String date,
                                               @RequestParam(required = false) String city,
                                               @RequestParam(required = false) String sort) throws ParseException{
        List<WeatherDto> weatherDtos = new ArrayList<>();
        if(date != null || city != null || sort != null){
            if(date != null){
                weatherDtos = weatherService.getWeatherFilter(date, serviceEnum.ByDate);
            }
            if(city != null){
                weatherDtos = weatherService.getWeatherFilter(city, serviceEnum.ByCity);
            }
            if(sort != null){
                weatherDtos = weatherService.getWeatherFilter(sort, serviceEnum.Sort);
            }
            return ResponseEntity.ok(weatherDtos);
        }

        weatherDtos = weatherService.getAllOrderById();
        return ResponseEntity.ok().body(weatherDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWeatherById(@PathVariable Integer id){

        WeatherDto weatherDto = weatherService.getWeatherUsingById(id);
        if(weatherDto == null){
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok().body(weatherDto);
        }
    }

}