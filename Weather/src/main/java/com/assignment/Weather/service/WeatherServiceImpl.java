package com.assignment.Weather.service;

import com.assignment.Weather.repository.WeatherRepository;
import com.assignment.Weather.model.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WeatherServiceImpl implements WeatherService {
    @Autowired
    WeatherRepository weatherRepository;

    @Override
    public Weather saveReading(Weather weather) {
        return weatherRepository.save(weather);
    }

    @Override
    public Optional<Double> findAvg(LocalDateTime from, LocalDateTime to) {
        return weatherRepository.findAvg(from, to);
    }

    @Override
    public Optional<Double> findAvgBySensorId(String sensorId, LocalDateTime from, LocalDateTime to) {
        return weatherRepository.findAvgBySensorId(sensorId, from, to);
    }
}
