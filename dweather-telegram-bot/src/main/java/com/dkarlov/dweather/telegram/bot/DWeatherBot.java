package com.dkarlov.dweather.telegram.bot;

import com.dkarlov.dweather.telegram.bot.command.CreateEventCommand;
import com.dkarlov.dweather.telegram.bot.command.DoneCommand;
import com.dkarlov.dweather.telegram.bot.command.PrecipitationCommand;
import com.dkarlov.dweather.telegram.bot.command.TemperatureCommand;
import com.dkarlov.dweather.telegram.bot.command.UserEventsCommand;
import com.dkarlov.dweather.telegram.bot.command.WindCommand;
import com.dkarlov.dweather.telegram.bot.command.processor.AbstractCommandResponseProcessor;
import com.dkarlov.dweather.telegram.bot.command.processor.UserEventsResponseProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class DWeatherBot extends TelegramLongPollingCommandBot {
    private static final String BOT_TOKEN = "1263286956:AAHyAsYALx7jYtotNqp9VGe9LQU8ZJdQ9Vs";
    private static final String BOT_NAME = "DWeatherBot";

    private final CreateEventCommand createEventCommand;
    private final TemperatureCommand temperatureCommand;
    private final WindCommand windCommand;
    private final PrecipitationCommand precipitationCommand;
    private final DoneCommand doneCommand;
    private final UserEventsCommand userEventsCommand;
    private final UserEventsResponseProcessor userEventsResponseProcessor;

    @Qualifier("precipitationResponseProcessor")
    private final AbstractCommandResponseProcessor precipitationResponseProcessor;

    @Qualifier("temperatureResponseProcessor")
    private final AbstractCommandResponseProcessor temperatureResponseProcessor;

    @Qualifier("windResponseProcessor")
    private final AbstractCommandResponseProcessor windResponseProcessor;

    @Value("${dweather.command.selection.temperature}")
    private String temperatureSelection;

    @Value("${dweather.command.selection.wind}")
    private String windSelection;

    @Value("${dweather.command.selection.precipitation}")
    private String precipitationSelection;

    @Value("${dweather.command.selection.events}")
    private String eventsSelection;

    public DWeatherBot(DefaultBotOptions botOptions, CreateEventCommand createEventCommand, TemperatureCommand temperatureCommand,
                       WindCommand windCommand, PrecipitationCommand precipitationCommand, DoneCommand doneCommand,
                       UserEventsCommand userEventsCommand, AbstractCommandResponseProcessor precipitationResponseProcessor,
                       AbstractCommandResponseProcessor temperatureResponseProcessor, AbstractCommandResponseProcessor windResponseProcessor,
                       UserEventsResponseProcessor userEventsResponseProcessor) {
        super(botOptions);
        this.createEventCommand = createEventCommand;
        this.temperatureCommand = temperatureCommand;
        this.windCommand = windCommand;
        this.precipitationCommand = precipitationCommand;
        this.doneCommand = doneCommand;
        this.userEventsCommand = userEventsCommand;
        this.precipitationResponseProcessor = precipitationResponseProcessor;
        this.temperatureResponseProcessor = temperatureResponseProcessor;
        this.windResponseProcessor = windResponseProcessor;
        this.userEventsResponseProcessor = userEventsResponseProcessor;
    }

    @PostConstruct
    public void registerCommands() {
        register(createEventCommand);
        register(temperatureCommand);
        register(windCommand);
        register(precipitationCommand);
        register(doneCommand);
        register(userEventsCommand);
        registerDefaultAction(((s, m) -> {
            log.info("Unknown command {}", m.getText());
            final SendMessage sendMessage = new SendMessage()
                    .setChatId(m.getChatId())
                    .setText(m.getText() + " command not found");
            try {
                s.execute(sendMessage);
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
            AnswerCallbackQuery answerCallbackQuery;
            final SendMessage sendMessage = new SendMessage()
                    .setChatId(update.getCallbackQuery().getMessage().getChatId());

            if (update.getCallbackQuery().getMessage().getText().equals(eventsSelection)) {
                answerCallbackQuery = processEvent(update);
            } else {
                answerCallbackQuery = processWeather(update);
            }
            sendMessage.setText(answerCallbackQuery.getText());

            try {
                execute(answerCallbackQuery);
                execute(sendMessage);
            } catch (TelegramApiException exception) {
                log.error("Error occurred while processing command respond", exception);
            }
        } else {
            log.info("Received unknown text: {}", update.getMessage().getText());
        }
    }

    private AnswerCallbackQuery processWeather(Update update) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        final String requestedParameter = update.getCallbackQuery().getMessage().getText();

        if (temperatureSelection.equals(requestedParameter)) {
            answerCallbackQuery = temperatureResponseProcessor.processCallbackResponse(update);
        } else if (windSelection.equals(requestedParameter)) {
            answerCallbackQuery = windResponseProcessor.processCallbackResponse(update);
        } else if (precipitationSelection.equals(requestedParameter)) {
            answerCallbackQuery = precipitationResponseProcessor.processCallbackResponse(update);
        } else if (eventsSelection.equals(requestedParameter)) {
            answerCallbackQuery = precipitationResponseProcessor.processCallbackResponse(update);
        }

        return answerCallbackQuery;
    }

    private AnswerCallbackQuery processEvent(Update update) {
        return userEventsResponseProcessor.processCallbackResponse(update);
    }
}