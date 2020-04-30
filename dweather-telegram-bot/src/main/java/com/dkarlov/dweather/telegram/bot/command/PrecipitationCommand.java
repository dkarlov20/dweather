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

import static com.dkarlov.dweather.telegram.bot.domain.Command.PRECIPITATION;
import static com.dkarlov.dweather.telegram.bot.domain.Precipitation.RAIN;
import static com.dkarlov.dweather.telegram.bot.domain.Precipitation.SNOW;

@Component
@Slf4j
public class PrecipitationCommand extends BotCommand {

    @Value("${dweather.command.selection.precipitation}")
    private String precipitationSelection;

    public PrecipitationCommand() {
        super(PRECIPITATION.name().toLowerCase(), PRECIPITATION.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        log.info("Setting precipitation for User {}", user.getId());

        sendReply(absSender, chat.getId());
    }

    private void sendReply(AbsSender absSender, long chatId) {
        final SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText(precipitationSelection)
                .setReplyMarkup(createReplyKeyboard());
        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException exception) {
            log.error("Error occurred while executing /" + PRECIPITATION.name().toLowerCase() + " command", exception);
        }
    }

    private InlineKeyboardMarkup createReplyKeyboard() {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        final List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        final List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText(RAIN.name().toLowerCase()).setCallbackData(RAIN.name().toLowerCase()));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText(SNOW.name().toLowerCase()).setCallbackData(SNOW.name().toLowerCase()));

        final List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}