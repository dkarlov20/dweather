package com.dkarlov.dweather.telegram.bot.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static com.dkarlov.dweather.telegram.bot.domain.Command.TEMPERATURE;

@Component
@Slf4j
public class TemperatureCommand extends BotCommand {
    private static final String THIRTY_PLUS = "30 +";
    private static final String TWENTY_TO_THIRTY = "20 - 30";
    private static final String TEN_TO_TWENTY = "10 - 20";
    private static final String ZERO_TO_TEN = "0 - 10";
    private static final String MINUS_TEN_TO_ZERO = "-10 - 0";
    private static final String MINUS_TWENTY_TO_MINUS_TEN = "-20 - -10";
    private static final String MINUS_THIRTY_TO_MINUS_TWENTY = "-30 - -20";
    private static final String MINUS_THIRTY_MINUS = "-30 -";

    @Value("${dweather.command.selection.temperature}")
    private String temperatureSelection;

    public TemperatureCommand() {
        super(TEMPERATURE.name().toLowerCase(), TEMPERATURE.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        log.info("Setting temperature for User {}", user.getId());

        sendReply(absSender, chat.getId());
    }

    private void sendReply(AbsSender absSender, long chatId) {
        final SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText(temperatureSelection)
                .setReplyMarkup(createReplyKeyboard());
        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException exception) {
            log.error("Error occurred while executing /" + TEMPERATURE.name().toLowerCase() + " command", exception);
        }
    }

    private InlineKeyboardMarkup createReplyKeyboard() {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        final List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        final List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        final List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        final List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText(THIRTY_PLUS).setCallbackData(THIRTY_PLUS));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText(TWENTY_TO_THIRTY).setCallbackData(TWENTY_TO_THIRTY));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText(TEN_TO_TWENTY).setCallbackData(TEN_TO_TWENTY));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText(ZERO_TO_TEN).setCallbackData(ZERO_TO_TEN));
        keyboardButtonsRow3.add(new InlineKeyboardButton().setText(MINUS_TEN_TO_ZERO).setCallbackData(MINUS_TEN_TO_ZERO));
        keyboardButtonsRow3.add(new InlineKeyboardButton().setText(MINUS_TWENTY_TO_MINUS_TEN).setCallbackData(MINUS_TWENTY_TO_MINUS_TEN));
        keyboardButtonsRow4.add(new InlineKeyboardButton().setText(MINUS_THIRTY_TO_MINUS_TWENTY).setCallbackData(MINUS_THIRTY_TO_MINUS_TWENTY));
        keyboardButtonsRow4.add(new InlineKeyboardButton().setText(MINUS_THIRTY_MINUS).setCallbackData(MINUS_THIRTY_MINUS));

        final List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        rowList.add(keyboardButtonsRow4);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}