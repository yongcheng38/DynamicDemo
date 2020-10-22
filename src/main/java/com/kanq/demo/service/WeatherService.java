package com.kanq.demo.service;


import com.kanq.demo.entity.Weather;
import com.kanq.demo.mapper.WeatherMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yyc
 */
@Service
public class WeatherService {

    @Resource
    private WeatherMapper weatherMapper;

    public boolean init() {
        weatherMapper.createDB();
        weatherMapper.createTable();
        return true;
    }

    public List<Weather> query(Long limit, Long offset) {
        return weatherMapper.select(limit, offset);
    }

    public int save(int temperature, float humidity) {
        Weather weather = new Weather();
        weather.setTemperature(temperature);
        weather.setHumidity(humidity);
        return weatherMapper.insert1(weather);
    }

    public int save(List<Weather> weatherList) {
        return weatherMapper.batchInsert(weatherList);
    }

}
