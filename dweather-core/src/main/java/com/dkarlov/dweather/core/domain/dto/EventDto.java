package com.dkarlov.dweather.core.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventDto {
    private Integer userId;
    private String userName;
    private Double temperature;
    private Double wind;
    private Boolean raining;
    private Boolean snowing;
}
