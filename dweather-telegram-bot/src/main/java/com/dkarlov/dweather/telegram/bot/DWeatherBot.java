package com.dkarlov.dweather.telegram.bot;

import com.dkarlov.dweather.telegram.bot.command.CreateEventCommand;
import com.dkarlov.dweather.telegram.bot.command.DoneCommand;
import com.dkarlov.dweather.telegram.bot.command.PrecipitationCommand;
import com.dkarlov.dweather.telegram.bot.command.TemperatureCommand;
import com.dkarlov.dweather.telegram.bot.command.WindCommand;
import com.dkarlov.dweather.telegram.bot.command.processor.CommandResponseProcessor;
import com.dkarlov.dweather.telegram.bot.domain.Weather;
import com.dkarlov.dweather.telegram.bot.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
@Slf4j
public class DWeatherBot extends TelegramLongPollingCommandBot {
    private static final String BOT_TOKEN = "1263286956:AAHyAsYALx7jYtotNqp9VGe9LQU8ZJdQ9Vs";
    private static final String BOT_NAME = "DWeatherBot";

    private final WeatherService weatherService;
    private final CreateEventCommand createEventCommand;
    private final TemperatureCommand temperatureCommand;
    private final WindCommand windCommand;
    private final PrecipitationCommand precipitationCommand;
    private final DoneCommand doneCommand;

    @Qualifier("precipitationResponseProcessor")
    private final CommandResponseProcessor precipitationResponseProcessor;

    @Qualifier("temperatureResponseProcessor")
    private final CommandResponseProcessor temperatureResponseProcessor;

    @Qualifier("windResponseProcessor")
    private final CommandResponseProcessor windResponseProcessor;

    @Value("${dweather.command.selection.temperature}")
    private String temperatureSelection;

    @Value("${dweather.command.selection.wind}")
    private String windSelection;

    @Value("${dweather.command.selection.precipitation}")
    private String precipitationSelection;

    public DWeatherBot(DefaultBotOptions botOptions, WeatherService weatherService, CreateEventCommand createEventCommand,
                       TemperatureCommand temperatureCommand, WindCommand windCommand, PrecipitationCommand precipitationCommand,
                       DoneCommand doneCommand, CommandResponseProcessor precipitationResponseProcessor,
                       CommandResponseProcessor temperatureResponseProcessor, CommandResponseProcessor windResponseProcessor) {
        super(botOptions);
        this.weatherService = weatherService;
        this.createEventCommand = createEventCommand;
        this.temperatureCommand = temperatureCommand;
        this.windCommand = windCommand;
        this.precipitationCommand = precipitationCommand;
        this.doneCommand = doneCommand;
        this.precipitationResponseProcessor = precipitationResponseProcessor;
        this.temperatureResponseProcessor = temperatureResponseProcessor;
        this.windResponseProcessor = windResponseProcessor;
    }

    @PostConstruct
    public void registerCommands() {
        register(createEventCommand);
        register(temperatureCommand);
        register(windCommand);
        register(precipitationCommand);
        register(doneCommand);
        registerDefaultAction(((s, m) -> {
            log.info("Unknown command {}", m.getText());
            final SendMessage message = new SendMessage()
                    .setChatId(m.getChatId())
                    .setText(m.getText() + " command not found");
            try {
                s.execute(message);
            } catch (TelegramApiException exception) {
                log.error("Error occurred while handling unknown command " + m.getText(), exception);
            }
        }));
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            final User user = update.getCallbackQuery().getFrom();
            log.info("Processing callback from User {}", user.getId());

            final Optional<Weather> weather = weatherService.getWeather(user);
            weather.ifPresentOrElse(w -> process(update.getCallbackQuery(), user, w),
                    () -> sendWarning(update.getCallbackQuery().getId()));
        } else {
            log.info("Received unknown text: {}", update.getMessage().getText());
        }
    }

    private void process(CallbackQuery callbackQuery, User user, Weather weather) {
        String requestedParameter = callbackQuery.getMessage().getText();
        String responseMessage;
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        final SendMessage message = new SendMessage()
                .setChatId(callbackQuery.getMessage().getChatId());

        if (temperatureSelection.equals(requestedParameter)) {
            responseMessage = "Temperature was set to: " + callbackQuery.getData();
            answerCallbackQuery = temperatureResponseProcessor.processCallbackResponse(user, weather, callbackQuery, responseMessage);
            message.setText(responseMessage);
        } else if (windSelection.equals(requestedParameter)) {
            responseMessage = "Wind was set to: " + callbackQuery.getData();
            answerCallbackQuery = windResponseProcessor.processCallbackResponse(user, weather, callbackQuery, responseMessage);
            message.setText(responseMessage);
        } else if (precipitationSelection.equals(requestedParameter)) {
            responseMessage = "Precipitation was set: " + callbackQuery.getData();
            answerCallbackQuery = precipitationResponseProcessor.processCallbackResponse(user, weather, callbackQuery, responseMessage);
            message.setText(responseMessage);
        }

        try {
            execute(answerCallbackQuery);
            execute(message);
        } catch (TelegramApiException exception) {
            log.error("Error occurred while processing command respond", exception);
        }
    }

    private void sendWarning(String callbackQueryId) {
        log.info("Event wasn`t found for callback id {}", callbackQueryId);

        String responseMessage = "Please create an event first";
        final AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery()
                .setText(responseMessage)
                .setCallbackQueryId(callbackQueryId);
        try {
            execute(answerCallbackQuery);
        } catch (TelegramApiException exception) {
            log.error("Error occurred while processing command respond", exception);
        }
    }
}