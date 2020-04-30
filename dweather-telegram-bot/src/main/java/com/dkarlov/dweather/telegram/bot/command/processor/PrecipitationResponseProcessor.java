package com.dkarlov.dweather.telegram.bot.command.processor;

import com.dkarlov.dweather.telegram.bot.domain.Precipitation;
import com.dkarlov.dweather.telegram.bot.domain.Weather;
import com.dkarlov.dweather.telegram.bot.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@Slf4j
public class PrecipitationResponseProcessor extends CommandResponseProcessor {

    protected PrecipitationResponseProcessor(WeatherService weatherService) {
        super(weatherService);
    }

    @Override
    protected void updateWeather(Weather weather, String data) {
        log.info("Updating precipitation with value {}", data);
        if (weather.getPrecipitation() == null) {
            weather.setPrecipitation(new HashSet<>());
        }
        weather.getPrecipitation().add(Precipitation.valueOf(data.toUpperCase()));
    }
}
