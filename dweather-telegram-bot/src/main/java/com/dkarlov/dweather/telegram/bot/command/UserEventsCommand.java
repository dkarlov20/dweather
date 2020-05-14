package com.dkarlov.dweather.telegram.bot.command;

import com.dkarlov.dweather.telegram.bot.domain.Event;
import com.dkarlov.dweather.telegram.bot.service.EventService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

import static com.dkarlov.dweather.telegram.bot.domain.Command.CREATE;
import static com.dkarlov.dweather.telegram.bot.domain.Command.EVENTS;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.util.Collections.singletonList;

@Component
public class UserEventsCommand extends AbstractBotCommand {
    private final EventService eventService;

    @Value("${dweather.command.selection.events}")
    private String eventsSelection;

    public UserEventsCommand(EventService eventService) {
        super(EVENTS);
        this.eventService = eventService;
    }

    @Override
    protected SendMessage processCommand(AbsSender absSender, User user, Chat chat, String[] arguments) {
        final SendMessage sendMessage = new SendMessage().setChatId(chat.getId());
        final List<Event> userEvents = eventService.getEventsByUserId(user.getId());

        if (userEvents.isEmpty()) {
            sendMessage.setText("You didn`t create any event.\nPlease use /" + CREATE.name().toLowerCase() + " to create a new one.");
        } else {
            sendMessage.setText(eventsSelection)
                    .setReplyMarkup(createReplyKeyboard(prepareButtons(userEvents)));
        }

        return sendMessage;
    }

    //TODO Add pagination
    private List<List<Pair<String, String>>> prepareButtons(List<Event> userEvents) {
        List<List<Pair<String, String>>> buttons = new ArrayList<>();
        for (Event userEvent : userEvents) {
            buttons.add(singletonList(Pair.of(userEvent.getId(), userEvent.getCreatedDate().format(ISO_DATE))));
        }

        return buttons;
    }
}
