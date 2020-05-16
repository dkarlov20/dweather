package com.dkarlov.dweather.telegram.bot.command.processor;

import com.dkarlov.dweather.telegram.bot.domain.DesiredWeather;
import com.dkarlov.dweather.telegram.bot.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Component
@Slf4j
public class TemperatureResponseProcessor extends AbstractCommandResponseProcessor {
    private final WeatherService weatherService;

    public TemperatureResponseProcessor(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Override
    protected String process(Update update) {
        final CallbackQuery callbackQuery = update.getCallbackQuery();
        final String data = callbackQuery.getData();
        final User user = callbackQuery.getFrom();
        final Optional<DesiredWeather> desiredWeatherOptional = weatherService.getDesiredWeather(user);

        return desiredWeatherOptional.map(desiredWeather -> updateTemperature(data, user, desiredWeather))
                .orElse("Please create an event first");
    }

    private String updateTemperature(String data, User user, DesiredWeather desiredWeather) {
        desiredWeather.setTemperature(Double.valueOf(data));
        weatherService.putDesiredWeather(user, desiredWeather);
        log.info("Temperature was set to {}", data);

        return "Temperature was set to: " + data;
    }
}
