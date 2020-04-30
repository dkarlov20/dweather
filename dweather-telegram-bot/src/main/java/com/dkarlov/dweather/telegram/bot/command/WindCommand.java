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

import static com.dkarlov.dweather.telegram.bot.domain.Command.WIND;

@Component
@Slf4j
public class WindCommand extends BotCommand {
    private static final String ZERO_TO_FIVE = "0 - 5";
    private static final String FIVE_TO_TEN = "5 - 10";
    private static final String TEN_TO_FIFTEEN = "10 - 15";
    private static final String FIFTEEN_TO_TWENTY = "15 - 20";
    private static final String TWENTY_TO_TWENTY_FIVE = "20 - 25";
    private static final String TWENTY_FIVE_PLUS = "25 +";

    @Value("${dweather.command.selection.wind}")
    private String windSelection;

    public WindCommand() {
        super(WIND.name().toLowerCase(), WIND.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        log.info("Setting wind for User {}", user.getId());
        sendReply(absSender, chat.getId());
    }

    private void sendReply(AbsSender absSender, long chatId) {
        final SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText(windSelection)
                .setReplyMarkup(createReplyKeyboard());
        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException exception) {
            log.error("Error occurred while executing /" + WIND.name().toLowerCase() + " command", exception);
        }
    }

    private InlineKeyboardMarkup createReplyKeyboard() {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        final List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        final List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        final List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText(ZERO_TO_FIVE).setCallbackData(ZERO_TO_FIVE));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText(FIVE_TO_TEN).setCallbackData(FIVE_TO_TEN));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText(TEN_TO_FIFTEEN).setCallbackData(TEN_TO_FIFTEEN));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText(FIFTEEN_TO_TWENTY).setCallbackData(FIFTEEN_TO_TWENTY));
        keyboardButtonsRow3.add(new InlineKeyboardButton().setText(TWENTY_TO_TWENTY_FIVE).setCallbackData(TWENTY_TO_TWENTY_FIVE));
        keyboardButtonsRow3.add(new InlineKeyboardButton().setText(TWENTY_FIVE_PLUS).setCallbackData(TWENTY_FIVE_PLUS));

        final List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}