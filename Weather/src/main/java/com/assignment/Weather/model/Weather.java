package com.assignment.Weather.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    String sensor_id;
    int temperature;
    String location;
    LocalDateTime reading_time;

    public Weather() {
    }

    public Weather(String sensorId, int temperature, String location, LocalDateTime readingTime) {
        this.sensor_id = sensorId;
        this.temperature = temperature;
        this.location = location;
        this.reading_time = readingTime;
    }

    public String getSensorId() {
        return sensor_id;
    }

    public void setSensorId(String sensorId) {
        this.sensor_id = sensorId;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getReadingTime() {
        return reading_time;
    }

    public void setReadingTime(LocalDateTime readingTime) {
        this.reading_time = readingTime;
    }
}
