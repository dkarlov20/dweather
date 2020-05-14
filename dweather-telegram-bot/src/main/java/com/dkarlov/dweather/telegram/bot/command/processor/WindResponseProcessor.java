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
public class WindResponseProcessor extends AbstractCommandResponseProcessor {
    private final WeatherService weatherService;

    public WindResponseProcessor(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Override
    protected String process(Update update) {
        final CallbackQuery callbackQuery = update.getCallbackQuery();
        final User user = callbackQuery.getFrom();
        final String data = callbackQuery.getData();

        final Optional<DesiredWeather> desiredWeatherOptional = weatherService.getDesiredWeather(user);
        if (desiredWeatherOptional.isPresent()) {
            final DesiredWeather desiredWeather = desiredWeatherOptional.get();
            desiredWeather.setWind(Double.valueOf(data));
            weatherService.putDesiredWeather(user, desiredWeather);

            log.info("Wind was set to {}", data);
            return "Wind was set to: " + data;
        }

        return "Please create an event first";
    }
}
