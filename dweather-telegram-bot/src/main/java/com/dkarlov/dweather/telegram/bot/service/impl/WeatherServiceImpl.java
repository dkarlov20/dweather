package com.dkarlov.dweather.telegram.bot.service.impl;

import com.dkarlov.dweather.telegram.bot.domain.DesiredWeather;
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
    private final Map<User, DesiredWeather> userDesiredWeather = new HashMap<>();

    @Override
    public Optional<DesiredWeather> getDesiredWeather(User user) {
        log.info("Pulling User {} from container", user.getId());
        return Optional.ofNullable(userDesiredWeather.get(user));
    }

    @Override
    public DesiredWeather putDesiredWeather(User user, DesiredWeather weather) {
        log.info("Adding User {} to container", user.getId());
        return userDesiredWeather.put(user, weather);
    }

    @Override
    public void removeDesiredWeather(User user) {
        log.info("Removing data for User {} from container", user.getId());
        userDesiredWeather.remove(user);
    }
}
