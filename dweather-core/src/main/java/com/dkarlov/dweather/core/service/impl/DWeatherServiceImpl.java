package com.dkarlov.dweather.core.service.impl;

import com.dkarlov.dweather.core.domain.DesiredWeather;
import com.dkarlov.dweather.core.domain.Event;
import com.dkarlov.dweather.core.domain.WeatherPerDay;
import com.dkarlov.dweather.core.domain.dto.EventDto;
import com.dkarlov.dweather.core.domain.dto.WeatherbitListResponseDto;
import com.dkarlov.dweather.core.domain.dto.WeatherbitResponseDto;
import com.dkarlov.dweather.core.domain.exception.DataNotFoundException;
import com.dkarlov.dweather.core.repository.EventRepository;
import com.dkarlov.dweather.core.service.DWeatherService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Service
@Slf4j
public class DWeatherServiceImpl implements DWeatherService {
    private static final double TEMPERATURE_DELTA = 5.0;
    private static final double WIND_DELTA = 2.0;
    private static final int QUORUM_PRECIPITATION_PROBABILITY = 50;
    private static final int QUORUM_ACCUMULATED_PRECIPITATION = 0;

    private final RestTemplate restTemplate;
    private final EventRepository eventRepository;

    @Value("${dweather.weatherbit.key}")
    private String key;

    @Value("${dweather.weatherbit.url}")
    private String url;

    @Value("${dweather.weatherbit.city}")
    private String city;

    public DWeatherServiceImpl(RestTemplate restTemplate, EventRepository eventRepository) {
        this.restTemplate = restTemplate;
        this.eventRepository = eventRepository;
    }

    @Override
    public Event saveEvent(EventDto eventDto) {
        log.info("Saving event for User {}", eventDto.getUserId());
        final Event event = Event.builder()
                .userId(eventDto.getUserId())
                .userName(eventDto.getUserName())
                .createdDate(LocalDateTime.now())
                .desiredWeather(DesiredWeather.builder()
                        .temperature(eventDto.getTemperature())
                        .wind(eventDto.getWind())
                        .raining(eventDto.getRaining())
                        .snowing(eventDto.getSnowing())
                        .build())
                .suitableDates(getSuitableDates(eventDto))
                .build();

        return eventRepository.saveEvent(event);
    }

    @Override
    public Event getEventById(ObjectId id) {
        log.info("Finding Event with id {}", id);
        return eventRepository.getEventById(id)
                .orElseThrow(() -> new DataNotFoundException("Event wasn't found for id " + id));
    }

    @Override
    public List<Event> getEventsByUserId(Integer userId) {
        log.info("Finding events for userId {}", userId);
        return eventRepository.getEventsByUserId(userId);
    }

    private List<WeatherPerDay> getSuitableDates(EventDto eventDto) {
        final ResponseEntity<WeatherbitListResponseDto> entity = restTemplate.getForEntity(buildUrl(), WeatherbitListResponseDto.class);
        final WeatherbitListResponseDto weatherbitListResponseDto = entity.getBody();

        if (weatherbitListResponseDto != null) {
            List<WeatherPerDay> suitableDates = weatherbitListResponseDto.getWeatherbitListResponseDto()
                    .stream()
                    .filter(buildPredicate(eventDto))
                    .map(w -> WeatherPerDay.builder()
                            .wind(w.getWind())
                            .temperature(w.getTemperature())
                            .date(w.getDate())
                            .raining(computePrecipitation(w.getPrecipitationProbability(), w.getAccumulatedRain()))
                            .snowing(computePrecipitation(w.getPrecipitationProbability(), w.getAccumulatedSnow()))
                            .build())
                    .collect(Collectors.toList());

            log.info("Found {} suitable dates for Event", suitableDates.size());
            return suitableDates;
        } else {
            log.info("Weather API didn`t return any data");
            return emptyList();
        }
    }

    private Predicate<WeatherbitResponseDto> buildPredicate(EventDto eventDto) {
        List<Predicate<WeatherbitResponseDto>> allPredicates = new ArrayList<>();

        if (eventDto.getTemperature() != null) {
            allPredicates.add(w -> isIndicatorInRange(eventDto.getTemperature(), w.getTemperature(), TEMPERATURE_DELTA));
        }
        if (eventDto.getWind() != null) {
            allPredicates.add(w -> isIndicatorInRange(eventDto.getWind(), w.getWind(), WIND_DELTA));
        }
        if (eventDto.getSnowing() != null && eventDto.getSnowing()) {
            allPredicates.add(w -> computePrecipitation(w.getPrecipitationProbability(), w.getAccumulatedSnow()));
        }
        if (eventDto.getRaining() != null && eventDto.getRaining()) {
            allPredicates.add(w -> computePrecipitation(w.getPrecipitationProbability(), w.getAccumulatedRain()));
        }

        return allPredicates.stream().reduce(x -> true, Predicate::and);
    }

    private boolean isIndicatorInRange(Double expectedIndicator, Double actualIndicator, Double delta) {
        return Range.between(expectedIndicator - delta, expectedIndicator + delta).contains(actualIndicator);
    }

    private boolean computePrecipitation(Double precipitationProbability, Double accumulatedPrecipitation) {
        return precipitationProbability > QUORUM_PRECIPITATION_PROBABILITY && accumulatedPrecipitation > QUORUM_ACCUMULATED_PRECIPITATION;
    }

    private String buildUrl() {
        return UriComponentsBuilder
                .fromHttpUrl(url)
                .queryParam("key", key)
                .queryParam("city", city)
                .toUriString();
    }
}
