package com.dkarlov.dweather.telegram.bot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

@Configuration
public class BotConfiguration {

    @Bean
    public DefaultBotOptions botOptions() {
        return ApiContext.getInstance(DefaultBotOptions.class);
    }
}
