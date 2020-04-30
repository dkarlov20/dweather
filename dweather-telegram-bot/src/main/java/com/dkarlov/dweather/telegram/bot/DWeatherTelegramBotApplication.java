package com.dkarlov.dweather.telegram.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@SpringBootApplication
@Slf4j
public class DWeatherTelegramBotApplication implements CommandLineRunner {
    private final DWeatherBot dWeatherBot;

    public DWeatherTelegramBotApplication(DWeatherBot dWeatherBot) {
        this.dWeatherBot = dWeatherBot;
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(DWeatherTelegramBotApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi();
            botsApi.registerBot(dWeatherBot);
        } catch (TelegramApiRequestException exception) {
            log.error("Error occurred while registering bot", exception);
        }
    }
}