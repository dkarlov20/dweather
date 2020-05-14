package com.dkarlov.dweather.telegram.bot.service;

import com.dkarlov.dweather.telegram.bot.domain.DesiredWeather;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

public interface WeatherService {
    Optional<DesiredWeather> getDesiredWeather(User user);

    DesiredWeather putDesiredWeather(User user, DesiredWeather weather);

    void removeDesiredWeather(User user);
}
