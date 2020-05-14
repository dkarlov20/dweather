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
import static com.dkarlov.dweather.telegram.bot.domain.Command.WIND;
import static com.google.common.collect.Lists.newArrayList;

@Component
@Slf4j
public class WindCommand extends AbstractBotCommand {
    private static final String ZERO = "0";
    private static final String THREE = "3";
    private static final String SIX = "6";
    private static final String NINE = "9";
    private static final String TWELVE = "12";
    private static final String FIFTEEN = "15";

    @Value("${dweather.command.selection.wind}")
    private String windSelection;

    private final WeatherService weatherService;

    public WindCommand(WeatherService weatherService) {
        super(WIND);
        this.weatherService = weatherService;
    }

    @Override
    protected SendMessage processCommand(AbsSender absSender, User user, Chat chat, String[] arguments) {
        final SendMessage sendMessage = new SendMessage().setChatId(chat.getId());
        final Optional<DesiredWeather> desiredWeatherOptional = weatherService.getDesiredWeather(user);
        desiredWeatherOptional.ifPresentOrElse(w -> {
                    log.info("Setting wind for User {}", user.getId());
                    sendMessage.setText(windSelection)
                            .setReplyMarkup(createReplyKeyboard(prepareButtons()));
                },
                () -> sendMessage.setText("You didn`t create any event.\nPlease use /" + CREATE.name().toLowerCase() + " to create a new one."));

        return sendMessage;
    }

    private List<List<Pair<String, String>>> prepareButtons() {
        return newArrayList(
                newArrayList(Pair.of(ZERO, ZERO), Pair.of(THREE, THREE)),
                newArrayList(Pair.of(SIX, SIX), Pair.of(NINE, NINE)),
                newArrayList(Pair.of(TWELVE, TWELVE), Pair.of(FIFTEEN, FIFTEEN))
        );
    }
}