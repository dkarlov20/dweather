package com.dkarlov.dweather.core.service;

import com.dkarlov.dweather.core.domain.Event;
import com.dkarlov.dweather.core.domain.dto.EventDto;
import org.bson.types.ObjectId;

import java.util.List;

public interface DWeatherService {
    Event saveEvent(EventDto eventDto);

    Event getEventById(ObjectId id);

    List<Event> getEventsByUserId(Integer userId);
}
