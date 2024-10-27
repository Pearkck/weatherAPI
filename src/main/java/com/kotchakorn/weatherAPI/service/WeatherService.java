package com.kotchakorn.weatherAPI.service;

import com.kotchakorn.weatherAPI.model.Weather;
import com.kotchakorn.weatherAPI.model.WeatherDto;
import com.kotchakorn.weatherAPI.repository.WeatherRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    private static final ModelMapper modelMapper = new ModelMapper();
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public WeatherDto createNewWeather(WeatherDto weatherDto) throws ParseException{
        weatherDto.setId(null);// create new
        formatter.setLenient(false);
//        formatter.setTimeZone(java.util.TimeZone.getTimeZone("ICT"));
        Weather weatherEntity = modelMapper.map(weatherDto, Weather.class);
        weatherEntity.setDate(formatter.parse(weatherDto.getDate()));
        System.out.println("date : " + weatherEntity.getDate());
        weatherEntity = weatherRepository.save(weatherEntity);
        return getWeatherDto(weatherEntity);
    }

    public List<WeatherDto> getWeatherFilter(String param, serviceEnum type) throws ParseException{
        List<Weather> weatherList = new ArrayList<>();
        switch (type){
            case ByDate:
               Date fotmateDate = formatter.parse(param);
               weatherList = weatherRepository.findAllByDate(fotmateDate);
               break;
            case ByCity:
                Comparator<Weather> ascendingOrder = (w1,w2) -> Integer.valueOf(w1.getId().compareTo(w2.getId()));
                List<String> citys = new ArrayList<>(Arrays.asList(param.split(",")));
                for(String city : citys){
                    weatherList.addAll(weatherRepository.findAllByCityIgnoreCase(city));
                }
                weatherList.sort(ascendingOrder);
                break;
            case Sort:
                if(param.equalsIgnoreCase("date"))
                    weatherList = weatherRepository.findAllByOrderByDateAsc();
                if(param.equalsIgnoreCase("-date"))
                    weatherList = weatherRepository.findAllByOrderByDateDesc();
                break;
            default:
                break;
        }
        return getListWeatherDto(weatherList);
    }

    public List<WeatherDto> getAllOrderById(){
        return getListWeatherDto(weatherRepository.findAllByOrderByIdAsc());
    }

    public WeatherDto getWeatherUsingById(Integer id){
        if(weatherRepository.existsById(id)){
            Weather wEntity = weatherRepository.findById(id).get();
            return getWeatherDto(wEntity);
        }else {
            return null;
        }
    }

    private List<WeatherDto> getListWeatherDto(List<Weather> wEntitys){
        formatter.setLenient(false);
        List<WeatherDto> weatherDtos = new ArrayList<WeatherDto>();
        for(Weather weatherEntity : wEntitys){
            weatherDtos.add(getWeatherDto(weatherEntity));
        }
        return weatherDtos;
    }

    private WeatherDto getWeatherDto(Weather weatherEntity){
        String formated_date = formatter.format(weatherEntity.getDate());
        WeatherDto w = modelMapper.map(weatherEntity, WeatherDto.class);
        w.setDate(formated_date);
        return w;
    }
}
