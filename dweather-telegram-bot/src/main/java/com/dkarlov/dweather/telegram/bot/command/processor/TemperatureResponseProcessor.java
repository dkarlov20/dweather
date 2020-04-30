package com.dkarlov.dweather.telegram.bot.command.processor;

import com.dkarlov.dweather.telegram.bot.domain.Weather;
import com.dkarlov.dweather.telegram.bot.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TemperatureResponseProcessor extends CommandResponseProcessor {

    protected TemperatureResponseProcessor(WeatherService weatherService) {
        super(weatherService);
    }

    @Override
    protected void updateWeather(Weather weather, String data) {
        log.info("Updating temperature with value {}", data);
        weather.setTemperature(data);
    }
}
