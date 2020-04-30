package com.dkarlov.dweather.telegram.bot.domain;

import lombok.Data;

import java.util.Set;

@Data
public class Weather {
    private String temperature;
    private String wind;
    private Set<Precipitation> precipitation;
}
