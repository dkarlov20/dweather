package com.dkarlov.dweather.telegram.bot.service;

import com.dkarlov.dweather.telegram.bot.domain.Weather;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

public interface WeatherService {
    Optional<Weather> getWeather(User user);

    Weather putWeather(User user, Weather weather);

    void removeWeather(User user);
}
