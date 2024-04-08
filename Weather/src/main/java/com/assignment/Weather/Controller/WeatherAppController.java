package com.assignment.Weather.Controller;

import com.assignment.Weather.model.Weather;
import com.assignment.Weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/WeatherApp")
public class WeatherAppController {
    @Autowired
    WeatherService weatherService;

    @GetMapping("/Welcome")
    public ResponseEntity<String> testWork() {
        return ResponseEntity.ok("Welcome to My Weather App !!!");
    }

    @PostMapping("/save-a-reading")
    public ResponseEntity<String> recordReading(@RequestBody Weather weather) {
        if (isFuture(weather.getReadingTime())) {
            return ResponseEntity.badRequest()
                    .body("Date cannot be in the future.");
        }

        Weather savedWeather = weatherService.saveReading(weather);
        if (savedWeather == null)
            return ResponseEntity.badRequest().body("Reading not saved.");
        else
            return ResponseEntity.ok("Reading saved successfully for sensor " +
                savedWeather.getSensorId());
    }

    @GetMapping("/avg-reading/{from}/{to}")
    public ResponseEntity<String> getAvgReading(@RequestParam(required = false) String sensorId,
                                                   @PathVariable(value = "from") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime from,
                                                   @PathVariable(value = "to") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime to) {

        if (isFuture(from) || isFuture(to))
            return ResponseEntity.badRequest()
                    .body("Dates cannot be in the future.");

        if (sensorId == null) {
            Optional<Double> averageTemp = weatherService.findAvg(from, to);
            return averageTemp.map(aDouble -> ResponseEntity.ok("The average temperature reported from " +
                            "all the sensors between " +
                            from + " & " + to + " is " +
                            aDouble +
                            " degree celsius."))
                    .orElseGet(() -> ResponseEntity.badRequest()
                            .body("No temperature values reported for the given dates."));
        } else {
            Optional<Double> averageTemp = weatherService.findAvgBySensorId(sensorId, from, to);
            return averageTemp.map(aDouble -> ResponseEntity.ok("Sensor " + sensorId +
                            " reported an average temperature of " +
                            aDouble +
                            " degree celsius between " +
                            from + " & " + to))
                    .orElseGet(() -> ResponseEntity.badRequest()
                            .body("No temperature values reported for the given dates for sensor " + sensorId));
        }
    }

    @GetMapping("/avg-reading-date-only/{from}/{to}")
    public ResponseEntity<String> getAvgReadingDateOnly(@RequestParam(required = false) String sensorId,
                                                        @PathVariable(value = "from") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate from,
                                                        @PathVariable(value = "to") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate to) {
        LocalDateTime fromTime = from.atStartOfDay();
        LocalDateTime toTime;
        if (to.isEqual(LocalDate.now()))
            toTime = LocalDateTime.now();
        else
            toTime = to.atTime(23, 59, 59);
        if (isFuture(fromTime) || isFuture(toTime))
            return ResponseEntity.badRequest()
                    .body("Dates cannot be in the future.");

        if (sensorId == null) {
            Optional<Double> averageTemp = weatherService.findAvg(from.atStartOfDay(), toTime);
            return averageTemp.map(aDouble -> ResponseEntity.ok("The average temperature reported from " +
                    "all the sensors between " +
                    from + " & " + to + " is " +
                    aDouble +
                    " degree celsius.")).orElseGet(() -> ResponseEntity.badRequest()
                    .body("No temperature values reported for the given dates."));
        } else {
            Optional<Double> averageTemp = weatherService.findAvgBySensorId(sensorId, fromTime, toTime);
            return averageTemp.map(aDouble -> ResponseEntity.ok("Sensor " + sensorId +
                    " reported an average temperature of " +
                    aDouble +
                    " degree celsius between " + from + " & " + to)).orElseGet(() -> ResponseEntity.badRequest()
                    .body("No temperature values reported for the given dates for sensor " + sensorId));
        }
    }

    public boolean isFuture(LocalDateTime date) {
        return date.isAfter(LocalDateTime.now());
    }
}
