package com.dkarlov.dweather.telegram.bot.command;

import com.dkarlov.dweather.telegram.bot.domain.Precipitation;
import com.dkarlov.dweather.telegram.bot.domain.Weather;
import com.dkarlov.dweather.telegram.bot.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Iterator;

import static com.dkarlov.dweather.telegram.bot.domain.Command.CREATE;
import static com.dkarlov.dweather.telegram.bot.domain.Command.DONE;
import static org.apache.commons.lang3.StringUtils.isNoneEmpty;

@Component
@Slf4j
public class DoneCommand extends BotCommand {
    private final WeatherService weatherService;

    public DoneCommand(WeatherService weatherService) {
        super(DONE.name(), DONE.getDescription());
        this.weatherService = weatherService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        log.info("Save event for User {}", user.getId());

        sendReply(absSender, user, chat.getId());
    }

    private void sendReply(AbsSender absSender, User user, long chatId) {
        final SendMessage message = new SendMessage()
                .setChatId(chatId);

        weatherService.getWeather(user).ifPresentOrElse(w -> message.setText(getResultMessage(w)),
                () -> {
                    log.info("For User {} any event wasn`t found", user.getId());
                    message.setText("You didn`t create any event.\nPlease use /" + CREATE.name().toLowerCase() + " to create a new one.");
                });

        try {
            absSender.execute(message);
        } catch (TelegramApiException exception) {
            log.error("Error occurred while executing /" + DONE.name().toLowerCase() + " command", exception);
        }
    }

    private String getResultMessage(Weather weather) {
        final StringBuilder response = new StringBuilder();
        response.append("Event was created:\n");
        if (isNoneEmpty(weather.getTemperature())) {
            response.append("Temperature: ").append(weather.getTemperature()).append("\n");
        }
        if (isNoneEmpty(weather.getWind())) {
            response.append("Wind: ").append(weather.getWind()).append("\n");
        }
        if (weather.getPrecipitation() != null) {
            response.append("Precipitation: ");
            Iterator<Precipitation> iterator = weather.getPrecipitation().iterator();
            while (iterator.hasNext()) {
                response.append(iterator.next().name().toLowerCase());
                if (iterator.hasNext()) {
                    response.append(", ");
                }
            }
        }

        return response.toString();
    }
}