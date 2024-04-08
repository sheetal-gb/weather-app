package com.assignment.Weather.Controller;

import com.assignment.Weather.model.Weather;
import com.assignment.Weather.repository.WeatherRepository;
import com.assignment.Weather.service.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WeatherAppController.class)
public class WeatherAppControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WeatherService weatherService;

    @MockBean
    WeatherRepository weatherRepository;

    @Test
    public void greeting_endpoint_test() throws Exception {
        mvc.perform(get("/WeatherApp/Welcome"))
                .andExpect(status().isOk());
    }

    @Test
    public void reading_endpoint_test() throws Exception {

        Weather newObjectInstance = new Weather("India",35,"Mumbai", LocalDateTime.now());
        Mockito.when(weatherService.saveReading(any(Weather.class)))
                .thenReturn(newObjectInstance);

        mvc.perform(MockMvcRequestBuilders.post("/WeatherApp/save-a-reading")
                .content(asJsonString(newObjectInstance))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void average_for_all_sensors_endpoint_test() throws Exception {
        Mockito.when(
                weatherService.findAvg(LocalDate.of(2024,2,18).atStartOfDay(),
                        LocalDate.of(2024,2,18).atTime(23,59,59)))
                        .thenReturn(Optional.of(32.5));

        mvc.perform(get("/WeatherApp/avg-reading-date-only/{from}/{to}",
                        LocalDate.of(2024,2,18),
                        LocalDate.of(2024,2,18))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("The average temperature reported from all the sensors between 2024-02-18 & 2024-02-18 is 32.5 degree celsius."));
        ;
    }

    @Test
    public void average_for_specific_sensor_endpoint_test() throws Exception {
        Mockito.when(
                        weatherService.findAvgBySensorId("Ireland",
                                LocalDate.of(2024,2,18).atStartOfDay(),
                                LocalDate.of(2024,2,18).atTime(23,59,59)))
                .thenReturn(Optional.of(8.5));

        mvc.perform(get("/WeatherApp/avg-reading-date-only/{from}/{to}?sensorId=Ireland",
                        LocalDate.of(2024,2,18),
                        LocalDate.of(2024,2,18))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Sensor Ireland reported an average temperature of 8.5 degree celsius between 2024-02-18 & 2024-02-18"));
    }

    public String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
