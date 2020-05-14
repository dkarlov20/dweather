package com.dkarlov.dweather.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WeatherbitListResponseDto {

    @JsonProperty("data")
    private List<WeatherbitResponseDto> weatherbitListResponseDto;
}
