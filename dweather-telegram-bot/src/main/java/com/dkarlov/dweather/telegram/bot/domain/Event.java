package com.dkarlov.dweather.telegram.bot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.joining;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private String id;
    private int userId;
    private String userName;
    private DesiredWeather desiredWeather;
    private List<WeatherPerDay> suitableDates;
    private LocalDateTime createdDate;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Event:\n");
        if (desiredWeather != null) {
            stringBuilder.append("Desired weather:\n").append(desiredWeather);
        }
        if (suitableDates != null) {
            if (suitableDates.isEmpty()) {
                stringBuilder.append("\nSuitable dates for this event weren`t found");
            } else {
                stringBuilder.append("\nSuitable dates:\n").append(suitableDates.stream()
                        .map(WeatherPerDay::getDate)
                        .collect(joining(", ")));
            }
        }
        return stringBuilder.toString();
    }
}
