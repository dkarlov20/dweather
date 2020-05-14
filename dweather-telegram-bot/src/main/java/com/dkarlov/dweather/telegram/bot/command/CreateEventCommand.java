package com.dkarlov.dweather.telegram.bot.command;

import com.dkarlov.dweather.telegram.bot.domain.DesiredWeather;
import com.dkarlov.dweather.telegram.bot.service.WeatherService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.dkarlov.dweather.telegram.bot.domain.Command.CREATE;
import static com.dkarlov.dweather.telegram.bot.domain.Command.DONE;
import static com.dkarlov.dweather.telegram.bot.domain.Command.PRECIPITATION;
import static com.dkarlov.dweather.telegram.bot.domain.Command.TEMPERATURE;
import static com.dkarlov.dweather.telegram.bot.domain.Command.WIND;

@Component
public class CreateEventCommand extends AbstractBotCommand {
    private final WeatherService weatherService;

    public CreateEventCommand(WeatherService weatherService) {
        super(CREATE);
        this.weatherService = weatherService;
    }

    @Override
    protected SendMessage processCommand(AbsSender absSender, User user, Chat chat, String[] arguments) {
        weatherService.putDesiredWeather(user, new DesiredWeather());
        return new SendMessage()
                .setChatId(chat.getId())
                .setText(getInstructionMessage());
    }

    private String getInstructionMessage() {
        return new StringBuilder().append("Started creating an event.\n\n")
                .append("To set desired parameters use following commands:\n")
                .append("1. Temperature: /").append(TEMPERATURE.name().toLowerCase()).append("\n")
                .append("2. Wind: /").append(WIND.name().toLowerCase()).append("\n")
                .append("3. Precipitation: /").append(PRECIPITATION.name().toLowerCase()).append("\n\n")
                .append("To finish creating an event use /").append(DONE.name().toLowerCase())
                .toString();
    }
}
