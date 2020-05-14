package com.dkarlov.dweather.telegram.bot.command.processor;

import com.dkarlov.dweather.telegram.bot.domain.Event;
import com.dkarlov.dweather.telegram.bot.service.EventService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
public class UserEventsResponseProcessor extends AbstractCommandResponseProcessor {
    private final EventService eventService;

    public UserEventsResponseProcessor(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    protected String process(Update update) {
        final CallbackQuery callbackQuery = update.getCallbackQuery();
        final Optional<Event> userEventOptional = eventService.getEventById(callbackQuery.getData());

        return userEventOptional.map(event -> "Event:\n" + event)
                .orElse("No event was found");
    }
}
