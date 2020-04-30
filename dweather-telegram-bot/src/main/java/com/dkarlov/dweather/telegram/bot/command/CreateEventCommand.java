package com.dkarlov.dweather.telegram.bot.command;

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

import static com.dkarlov.dweather.telegram.bot.domain.Command.CREATE;
import static com.dkarlov.dweather.telegram.bot.domain.Command.DONE;
import static com.dkarlov.dweather.telegram.bot.domain.Command.PRECIPITATION;
import static com.dkarlov.dweather.telegram.bot.domain.Command.TEMPERATURE;
import static com.dkarlov.dweather.telegram.bot.domain.Command.WIND;

@Component
@Slf4j
public class CreateEventCommand extends BotCommand {
    private final WeatherService weatherService;

    public CreateEventCommand(WeatherService weatherService) {
        super(CREATE.name().toLowerCase(), CREATE.getDescription());
        this.weatherService = weatherService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        weatherService.putWeather(user, new Weather());
        log.info("Created new entry for User {}", user.getId());
        sendReply(absSender, chat.getId());
    }

    private void sendReply(AbsSender absSender, long chatId) {
        final SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(getInstructionMessage());

        try {
            absSender.execute(message);
        } catch (TelegramApiException exception) {
            log.error("Error occurred while executing /" + CREATE.name().toLowerCase() + " command", exception);
        }
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
