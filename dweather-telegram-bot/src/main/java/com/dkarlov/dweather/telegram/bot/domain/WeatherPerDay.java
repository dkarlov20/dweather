package com.dkarlov.dweather.telegram.bot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherPerDay {
    private Double temperature;
    private Double wind;
    private Boolean raining;
    private Boolean snowing;
    private String date;
}
