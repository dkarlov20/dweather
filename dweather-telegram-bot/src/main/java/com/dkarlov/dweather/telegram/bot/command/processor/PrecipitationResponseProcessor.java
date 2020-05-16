package com.dkarlov.dweather.telegram.bot.command.processor;

import com.dkarlov.dweather.telegram.bot.domain.DesiredWeather;
import com.dkarlov.dweather.telegram.bot.domain.Precipitation;
import com.dkarlov.dweather.telegram.bot.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Component
@Slf4j
public class PrecipitationResponseProcessor extends AbstractCommandResponseProcessor {
    private final WeatherService weatherService;

    protected PrecipitationResponseProcessor(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Override
    protected String process(Update update) {
        final CallbackQuery callbackQuery = update.getCallbackQuery();
        final User user = callbackQuery.getFrom();
        final String data = callbackQuery.getData();
        final Optional<DesiredWeather> desiredWeatherOptional = weatherService.getDesiredWeather(user);

        return desiredWeatherOptional.map(desiredWeather -> updatePrecipitation(user, data, desiredWeather))
                .orElse("Please create an event first");
    }

    private String updatePrecipitation(User user, String data, DesiredWeather desiredWeather) {
        switch (Precipitation.valueOf(data.toUpperCase())) {
            case SNOW:
                desiredWeather.setSnowing(true);
                log.info("Snowing was set ot true");
                break;
            case RAIN:
                desiredWeather.setRaining(true);
                log.info("Raining was set ot true");
                break;
        }
        weatherService.putDesiredWeather(user, desiredWeather);

        return "Add precipitation: " + data;
    }
}
