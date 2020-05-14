package com.dkarlov.dweather.core.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DWeatherErrorResponse {
    private String message;
    private String createdTime;
}
