package com.dkarlov.dweather.telegram.bot.domain;

public enum Command {
    CREATE("Create a new event with necessary weather conditions"),
    WIND("Desired wind speed interval (m/s)"),
    TEMPERATURE("Desired temperature interval (c)"),
    PRECIPITATION("Desired precipitations"),
    DONE("Save created event");

    private final String description;

    Command(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
