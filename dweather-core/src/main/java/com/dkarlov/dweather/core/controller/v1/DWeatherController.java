package com.dkarlov.dweather.core.controller.v1;

import com.dkarlov.dweather.core.domain.Event;
import com.dkarlov.dweather.core.domain.dto.EventDto;
import com.dkarlov.dweather.core.service.DWeatherService;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/dweather")
public class DWeatherController {
    private final DWeatherService dWeatherService;

    public DWeatherController(DWeatherService dWeatherService) {
        this.dWeatherService = dWeatherService;
    }

    @PostMapping(value = "/events", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Event saveEvent(@RequestBody EventDto eventDto) {
        return dWeatherService.saveEvent(eventDto);
    }

    @GetMapping(value = "/events/{eventId}", produces = APPLICATION_JSON_VALUE)
    public Event getEvent(@PathVariable String eventId) {
        return dWeatherService.getEventById(new ObjectId(eventId));
    }

    @GetMapping(value = "/events", produces = APPLICATION_JSON_VALUE)
    public List<Event> getEventByUserId(@RequestParam(name = "user_id") Integer userId) {
        return dWeatherService.getEventsByUserId(userId);
    }
}
