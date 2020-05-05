package com.dkarlov.dweather.telegram.bot.service.impl;

import com.dkarlov.dweather.telegram.bot.domain.Weather;
import com.dkarlov.dweather.telegram.bot.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {
    private final Map<User, Weather> userWeather = new HashMap<>();

    @Override
    public Optional<Weather> getWeather(User user) {
        log.info("Pulling User {} from container", user.getId());
        return Optional.ofNullable(userWeather.get(user));
    }

    @Override
    public Weather putWeather(User user, Weather weather) {
        log.info("Adding User {} to container", user.getId());
        return userWeather.put(user, weather);
    }

    @Override
    public void removeWeather(User user) {
        log.info("Removing data for User {} from container", user.getId());
        userWeather.remove(user);
    }
}
