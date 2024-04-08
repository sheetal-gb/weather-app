package com.assignment.Weather.Repository;

import com.assignment.Weather.model.Weather;
import com.assignment.Weather.repository.WeatherRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class WeatherRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WeatherRepository weatherRepository;

    @Test
    public void save_a_reading_test() {
        Weather weather1 = new Weather("India", 35, "Mumbai", LocalDateTime.of(2024, 2, 18,0,0));
        Weather weather = weatherRepository.save(weather1);

        assertThat(weather).hasFieldOrPropertyWithValue("sensorId", "India");
        assertThat(weather).hasFieldOrPropertyWithValue("temperature", 35);
        assertThat(weather).hasFieldOrPropertyWithValue("location", "Mumbai");
        assertThat(weather).hasFieldOrPropertyWithValue("readingTime", LocalDateTime.of(2024,2,18,0,0));
    }

    @Test
    public void find_avg_of_all_sensors_test() {
        // given
        loadReadings();

        // when
        Optional<Double> found = weatherRepository.findAvg(LocalDateTime.of(2024,1,1,0,0),
                LocalDateTime.now());

        // then
        found.ifPresent(aDouble -> assertThat(aDouble).isEqualTo(30));
    }

    @Test
    public void find_avg_of_specific_sensor_test() {
        // given
        loadReadings();

        // when
        Optional<Double> found = weatherRepository.findAvgBySensorId("India", LocalDateTime.of(2024,1,1,0,0),
                LocalDateTime.now());

        // then
        found.ifPresent(aDouble -> assertThat(aDouble).isEqualTo(32.5));
    }

    public void loadReadings() {
        Weather weather1 = new Weather("India", 35, "Mumbai", LocalDateTime.of(2024, 2, 18,0,0));
        entityManager.persist(weather1);

        Weather weather2 = new Weather("Korea", 25, "Seoul", LocalDateTime.of(2024, 3, 9,0,0));
        entityManager.persist(weather2);

        Weather weather3 = new Weather("India", 30, "Pune", LocalDateTime.now());
        entityManager.persist(weather3);
    }
}
