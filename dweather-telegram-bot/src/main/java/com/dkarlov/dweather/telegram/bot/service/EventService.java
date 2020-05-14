package com.dkarlov.dweather.telegram.bot.service;

import com.dkarlov.dweather.telegram.bot.domain.Event;
import com.dkarlov.dweather.telegram.bot.domain.dto.EventDto;

import java.util.List;
import java.util.Optional;

public interface EventService {
    Event saveEvent(EventDto event);

    List<Event> getEventsByUserId(int userId);

    Optional<Event> getEventById(String eventId);
}
