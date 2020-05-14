package com.dkarlov.dweather.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeatherbitResponseDto {

    @JsonProperty("temp")
    private Double temperature;

    @JsonProperty("wind_spd")
    private Double wind;

    @JsonProperty("pop")
    private Double precipitationProbability;

    @JsonProperty("snow")
    private Double accumulatedSnow;

    @JsonProperty("precip")
    private Double accumulatedRain;

    @JsonProperty("valid_date")
    private String date;
}
