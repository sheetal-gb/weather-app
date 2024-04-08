package com.assignment.Weather.repository;

import com.assignment.Weather.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Integer> {
    @Query("SELECT AVG(temperature) FROM Weather " +
            "WHERE sensor_id = ?1 AND reading_time BETWEEN ?2 AND ?3")
    Optional<Double> findAvgBySensorId(String sensorId, LocalDateTime from, LocalDateTime to);

    @Query("SELECT AVG(temperature) FROM Weather " +
            "WHERE reading_time BETWEEN ?1 AND ?2")
    Optional<Double> findAvg(LocalDateTime from, LocalDateTime to);
}
