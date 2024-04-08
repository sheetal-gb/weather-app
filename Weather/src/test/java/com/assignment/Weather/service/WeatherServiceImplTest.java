package com.assignment.Weather.service;

import com.assignment.Weather.model.Weather;
import com.assignment.Weather.repository.WeatherRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
public class WeatherServiceImplTest {
    @TestConfiguration
    static class WeatherServiceImplTestContextConfig {
        @Bean
        public WeatherService weatherService() {
            return new WeatherServiceImpl();
        }
    }

    @Autowired
    private WeatherService weatherService;

    @MockBean
    private WeatherRepository weatherRepository;

    @Before
    public void setUp() {
        Mockito.when(weatherRepository.save(Mockito.any(Weather.class)))
                        .thenReturn(new Weather("India", 35, "Mumbai", LocalDateTime.of(2024,1,1,0,0)));

        Mockito.when(weatherRepository.findAvg(LocalDateTime.of(2024,1,1,0,0),
                LocalDateTime.now()))
                .thenReturn(Optional.of(32.5));

        Mockito.when(weatherRepository.findAvgBySensorId("India", LocalDateTime.of(2024,1,1,0,0),
                LocalDateTime.now()))
                .thenReturn(Optional.of(30.5));
    }

    @Test
    public void save_a_reading() {
        Weather weather = new Weather("India", 35, "Mumbai", LocalDateTime.of(2024,1,1,0,0));
        Weather saved_weather = weatherService.saveReading(weather);

        assertThat(saved_weather.getSensorId()).isEqualTo("India");
        assertThat(saved_weather.getLocation()).isEqualTo("Mumbai");
        assertThat(saved_weather.getTemperature()).isEqualTo(35);
        assertThat(saved_weather.getReadingTime()).isEqualTo(LocalDateTime.of(2024,1,1,0,0));
    }

    @Test
    public void find_avg_of_all_sensors_test() {
        Optional<Double> average = Optional.of(32.5);
         Optional<Double> found = weatherService.findAvg(LocalDateTime.of(2024,1,1,0,0),
                LocalDateTime.now());
        found.ifPresent(aDouble -> assertThat(aDouble).isEqualTo(average.get()));
    }

    @Test
    public void find_avg_of_specific_sensor_test() {
        Optional<Double> average = Optional.of(30.5);
        Optional<Double> found = weatherService.findAvgBySensorId("India", LocalDateTime.of(2024,1,1,0,0),
                LocalDateTime.now());

        found.ifPresent(aDouble -> assertThat(aDouble).isEqualTo(average.get()));
    }
}
