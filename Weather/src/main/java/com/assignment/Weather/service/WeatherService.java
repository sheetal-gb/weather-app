package com.assignment.Weather.service;

import com.assignment.Weather.model.Weather;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WeatherService {
    Weather saveReading(Weather weather);

    Optional<Double> findAvg(LocalDateTime from, LocalDateTime to);

    Optional<Double> findAvgBySensorId(String sensorId, LocalDateTime from, LocalDateTime to);
}
