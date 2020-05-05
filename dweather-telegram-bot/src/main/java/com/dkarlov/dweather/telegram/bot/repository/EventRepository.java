package com.dkarlov.dweather.telegram.bot.repository;

import com.dkarlov.dweather.telegram.bot.domain.Event;
import org.bson.types.ObjectId;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Optional;

public interface EventRepository {
     Event saveEvent(Event event);

     List<Event> getUserEvents(User user);

     Optional<Event> getUserEvent(ObjectId eventId);
}
