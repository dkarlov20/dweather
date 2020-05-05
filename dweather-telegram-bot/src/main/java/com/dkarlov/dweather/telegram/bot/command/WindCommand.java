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
import static com.dkarlov.dweather.telegram.bot.domain.Command.WIND;
import static com.google.common.collect.Lists.newArrayList;

@Component
@Slf4j
public class WindCommand extends AbstractBotCommand {
    private static final String ZERO_TO_FIVE = "0 - 5";
    private static final String FIVE_TO_TEN = "5 - 10";
    private static final String TEN_TO_FIFTEEN = "10 - 15";
    private static final String FIFTEEN_TO_TWENTY = "15 - 20";
    private static final String TWENTY_TO_TWENTY_FIVE = "20 - 25";
    private static final String TWENTY_FIVE_PLUS = "25 +";

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
        final Optional<Weather> weatherOptional = weatherService.getWeather(user);
        weatherOptional.ifPresentOrElse(weather -> {
                    log.info("Setting wind for User {}", user.getId());
                    sendMessage.setText(windSelection)
                            .setReplyMarkup(createReplyKeyboard(prepareButtons()));
                },
                () -> sendMessage.setText("You didn`t create any event.\nPlease use /" + CREATE.name().toLowerCase() + " to create a new one."));

        return sendMessage;
    }

    private List<List<Pair<String, String>>> prepareButtons() {
        return newArrayList(
                newArrayList(Pair.of(ZERO_TO_FIVE, ZERO_TO_FIVE), Pair.of(FIVE_TO_TEN, FIVE_TO_TEN)),
                newArrayList(Pair.of(TEN_TO_FIFTEEN, TEN_TO_FIFTEEN), Pair.of(FIFTEEN_TO_TWENTY, FIFTEEN_TO_TWENTY)),
                newArrayList(Pair.of(TWENTY_TO_TWENTY_FIVE, TWENTY_TO_TWENTY_FIVE), Pair.of(TWENTY_FIVE_PLUS, TWENTY_FIVE_PLUS))
        );
    }
}