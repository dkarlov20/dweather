package com.dkarlov.dweather.telegram.bot.service.impl;

import com.dkarlov.dweather.telegram.bot.domain.Event;
import com.dkarlov.dweather.telegram.bot.repository.EventRepository;
import com.dkarlov.dweather.telegram.bot.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event saveEvent(Event event) {
        log.info("Saving event for User {}", event.getUserId());
        return eventRepository.saveEvent(event);
    }

    @Override
    public List<Event> getUserEvents(User user) {
        log.info("Getting events for User {}", user.getId());
        return eventRepository.getUserEvents(user);
    }

    @Override
    public Optional<Event> getUserEventById(ObjectId eventId) {
        log.info("Getting event by id {}", eventId);
        return eventRepository.getUserEvent(eventId);
    }
}
