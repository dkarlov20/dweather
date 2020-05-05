package com.dkarlov.dweather.telegram.bot.command;

import com.dkarlov.dweather.telegram.bot.domain.Weather;
import com.dkarlov.dweather.telegram.bot.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;
import java.util.Optional;

import static com.dkarlov.dweather.telegram.bot.domain.Command.CREATE;
import static com.dkarlov.dweather.telegram.bot.domain.Command.TEMPERATURE;
import static com.google.common.collect.Lists.newArrayList;

@Component
@Slf4j
public class TemperatureCommand extends AbstractBotCommand {
    private static final String THIRTY_PLUS = "30 +";
    private static final String TWENTY_TO_THIRTY = "20 - 30";
    private static final String TEN_TO_TWENTY = "10 - 20";
    private static final String ZERO_TO_TEN = "0 - 10";
    private static final String MINUS_TEN_TO_ZERO = "-10 - 0";
    private static final String MINUS_TWENTY_TO_MINUS_TEN = "-20 - -10";
    private static final String MINUS_THIRTY_TO_MINUS_TWENTY = "-30 - -20";
    private static final String MINUS_THIRTY_MINUS = "-30 -";

    @Value("${dweather.command.selection.temperature}")
    private String temperatureSelection;

    private final WeatherService weatherService;

    public TemperatureCommand(WeatherService weatherService) {
        super(TEMPERATURE);
        this.weatherService = weatherService;
    }

    @Override
    protected SendMessage processCommand(AbsSender absSender, User user, Chat chat, String[] arguments) {
        final SendMessage sendMessage = new SendMessage().setChatId(chat.getId());
        final Optional<Weather> weatherOptional = weatherService.getWeather(user);
        weatherOptional.ifPresentOrElse(weather -> {
                    log.info("Setting temperature for User {}", user.getId());
                    sendMessage.setText(temperatureSelection)
                            .setReplyMarkup(createReplyKeyboard(prepareButtons()));
                },
                () -> sendMessage.setText("You didn`t create any event.\nPlease use /" + CREATE.name().toLowerCase() + " to create a new one."));

        return sendMessage;
    }

    private List<List<Pair<String, String>>> prepareButtons() {
        return newArrayList(
                newArrayList(Pair.of(THIRTY_PLUS, THIRTY_PLUS), Pair.of(TWENTY_TO_THIRTY, TWENTY_TO_THIRTY)),
                newArrayList(Pair.of(TEN_TO_TWENTY, TEN_TO_TWENTY), Pair.of(ZERO_TO_TEN, ZERO_TO_TEN)),
                newArrayList(Pair.of(MINUS_TEN_TO_ZERO, MINUS_TEN_TO_ZERO), Pair.of(MINUS_TWENTY_TO_MINUS_TEN, MINUS_TWENTY_TO_MINUS_TEN)),
                newArrayList(Pair.of(MINUS_THIRTY_TO_MINUS_TWENTY, MINUS_THIRTY_TO_MINUS_TWENTY), Pair.of(MINUS_THIRTY_MINUS, MINUS_THIRTY_MINUS))
        );
    }
}