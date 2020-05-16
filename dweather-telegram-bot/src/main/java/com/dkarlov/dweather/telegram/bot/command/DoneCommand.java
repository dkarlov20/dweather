package com.dkarlov.dweather.telegram.bot.command;

import com.dkarlov.dweather.telegram.bot.domain.DesiredWeather;
import com.dkarlov.dweather.telegram.bot.domain.dto.EventDto;
import com.dkarlov.dweather.telegram.bot.service.EventService;
import com.dkarlov.dweather.telegram.bot.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

import static com.dkarlov.dweather.telegram.bot.domain.Command.CREATE;
import static com.dkarlov.dweather.telegram.bot.domain.Command.DONE;

@Component
@Slf4j
public class DoneCommand extends AbstractBotCommand {
    private final WeatherService weatherService;
    private final EventService eventService;

    public DoneCommand(WeatherService weatherService, EventService eventService) {
        super(DONE);
        this.weatherService = weatherService;
        this.eventService = eventService;
    }

    @Override
    protected SendMessage processCommand(AbsSender absSender, User user, Chat chat, String[] arguments) {
        final SendMessage sendMessage = new SendMessage()
                .setChatId(chat.getId());
        final Optional<DesiredWeather> desiredWeatherOptional = weatherService.getDesiredWeather(user);

        desiredWeatherOptional.ifPresentOrElse(w -> {
            sendMessage.setText("Event was created:\n" + eventService.saveEvent(buildEventDto(user, w)));
            weatherService.removeDesiredWeather(user);
        }, () -> {
            log.info("For User {} any event wasn`t found", user.getId());
            sendMessage.setText("You didn`t create any event.\nPlease use /" + CREATE.name().toLowerCase() + " to create a new one.");
        });

        return sendMessage;
    }

    private EventDto buildEventDto(User user, DesiredWeather w) {
        return EventDto.builder()
                .userId(user.getId())
                .userName(user.getUserName())
                .temperature(w.getTemperature())
                .wind(w.getWind())
                .raining(w.isRaining())
                .snowing(w.isSnowing())
                .build();
    }
}