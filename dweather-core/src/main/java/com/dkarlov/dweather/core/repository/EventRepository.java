package com.dkarlov.dweather.core.repository;

import com.dkarlov.dweather.core.domain.Event;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface EventRepository {
    Event saveEvent(Event event);

    List<Event> getEventsByUserId(Integer userId);

    Optional<Event> getEventById(ObjectId eventId);
}
