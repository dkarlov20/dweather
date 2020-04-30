package com.dkarlov.dweather.telegram.bot.command.processor;

import com.dkarlov.dweather.telegram.bot.domain.Weather;
import com.dkarlov.dweather.telegram.bot.service.WeatherService;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;

public abstract class CommandResponseProcessor {
    private final WeatherService weatherService;

    protected CommandResponseProcessor(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public AnswerCallbackQuery processCallbackResponse(User user, Weather weather, CallbackQuery callbackQuery, String responseMessage) {
        updateWeather(weather, callbackQuery.getData());
        weatherService.putWeather(user, weather);

        return new AnswerCallbackQuery()
                .setCallbackQueryId(callbackQuery.getId())
                .setText(responseMessage);
    }

    protected abstract void updateWeather(Weather weather, String data);
}
