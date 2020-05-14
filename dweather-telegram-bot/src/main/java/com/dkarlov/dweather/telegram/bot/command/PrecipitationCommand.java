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
import static com.dkarlov.dweather.telegram.bot.domain.Command.PRECIPITATION;
import static com.dkarlov.dweather.telegram.bot.domain.Precipitation.RAIN;
import static com.dkarlov.dweather.telegram.bot.domain.Precipitation.SNOW;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.capitalize;

@Component
@Slf4j
public class PrecipitationCommand extends AbstractBotCommand {
    private final WeatherService weatherService;

    @Value("${dweather.command.selection.precipitation}")
    private String precipitationSelection;

    public PrecipitationCommand(WeatherService weatherService) {
        super(PRECIPITATION);
        this.weatherService = weatherService;
    }

    @Override
    protected SendMessage processCommand(AbsSender absSender, User user, Chat chat, String[] arguments) {
        final SendMessage sendMessage = new SendMessage().setChatId(chat.getId());
        final Optional<DesiredWeather> desiredWeatherOptional = weatherService.getDesiredWeather(user);
        desiredWeatherOptional.ifPresentOrElse(w -> {
                    log.info("Setting precipitation for User {}", user.getId());
                    sendMessage.setText(precipitationSelection)
                            .setReplyMarkup(createReplyKeyboard(prepareButtons()));
                },
                () -> sendMessage.setText("You didn`t create any event.\nPlease use /" + CREATE.name().toLowerCase() + " to create a new one."));

        return sendMessage;
    }

    private List<List<Pair<String, String>>> prepareButtons() {
        return newArrayList(
                singletonList(Pair.of(capitalize(RAIN.name().toLowerCase()), capitalize(RAIN.name().toLowerCase()))),
                singletonList(Pair.of(capitalize(SNOW.name().toLowerCase()), capitalize(SNOW.name().toLowerCase())))
        );
    }
}