package com.dkarlov.dweather.telegram.bot.command;

import com.dkarlov.dweather.telegram.bot.domain.DesiredWeather;
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
    private static final String MINUS_THIRTY = "-30";
    private static final String MINUS_TWENTY = "-20";
    private static final String MINUS_TEN = "-10";
    private static final String ZERO = "0";
    private static final String TEN = "10";
    private static final String TWENTY = "20";
    private static final String THIRTY = "30";
    private static final String FORTY = "40";

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
        final Optional<DesiredWeather> desiredWeatherOptional = weatherService.getDesiredWeather(user);
        desiredWeatherOptional.ifPresentOrElse(w -> {
                    log.info("Setting temperature for User {}", user.getId());
                    sendMessage.setText(temperatureSelection)
                            .setReplyMarkup(createReplyKeyboard(prepareButtons()));
                },
                () -> sendMessage.setText("You didn`t create any event.\nPlease use /" + CREATE.name().toLowerCase() + " to create a new one."));

        return sendMessage;
    }

    private List<List<Pair<String, String>>> prepareButtons() {
        return newArrayList(
                newArrayList(Pair.of(MINUS_THIRTY, MINUS_THIRTY), Pair.of(MINUS_TWENTY, MINUS_TWENTY)),
                newArrayList(Pair.of(MINUS_TEN, MINUS_TEN), Pair.of(ZERO, ZERO)),
                newArrayList(Pair.of(TEN, TEN), Pair.of(TWENTY, TWENTY)),
                newArrayList(Pair.of(THIRTY, THIRTY), Pair.of(FORTY, FORTY))
        );
    }
}