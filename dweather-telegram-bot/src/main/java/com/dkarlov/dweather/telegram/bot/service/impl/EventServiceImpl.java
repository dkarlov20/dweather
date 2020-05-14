package com.dkarlov.dweather.telegram.bot.service.impl;

import com.dkarlov.dweather.telegram.bot.domain.Event;
import com.dkarlov.dweather.telegram.bot.domain.dto.EventDto;
import com.dkarlov.dweather.telegram.bot.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EventServiceImpl implements EventService {
    private final RestTemplate restTemplate;

    @Value("${dweather.api.url}")
    private String url;

    public EventServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Event saveEvent(EventDto event) {
        log.info("Saving event for User {}", event.getUserId());
        return restTemplate.postForObject(url, event, Event.class);
    }

    @Override
    public List<Event> getEventsByUserId(int userId) {
        log.info("Getting events for User {}", userId);
        ResponseEntity<List<Event>> responseEntity = restTemplate.exchange(
                UriComponentsBuilder.fromHttpUrl(url).queryParam("user_id", userId).toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Event>>() {
                });

        return responseEntity.getBody();
    }

    @Override
    public Optional<Event> getEventById(String eventId) {
        log.info("Getting event by id {}", eventId);

        return Optional.ofNullable(restTemplate.getForObject(url + "/" + eventId, Event.class));

    }
}
