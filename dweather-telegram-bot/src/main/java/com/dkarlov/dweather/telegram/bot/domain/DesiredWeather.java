package com.dkarlov.dweather.telegram.bot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DesiredWeather {
    private Double temperature;
    private Double wind;
    private boolean raining;
    private boolean snowing;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (temperature != null) {
            stringBuilder.append("Temperature: ").append(temperature).append("\n");
        }
        if (wind != null) {
            stringBuilder.append("Wind: ").append(wind).append("\n");
        }
        stringBuilder.append("Precipitation: \n");
        stringBuilder.append("- Raining: ").append(raining).append("\n");
        stringBuilder.append("- Snowing: ").append(snowing);

        return stringBuilder.toString();
    }
}
