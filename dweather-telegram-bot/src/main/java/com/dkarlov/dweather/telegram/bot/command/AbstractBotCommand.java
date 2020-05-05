package com.dkarlov.dweather.telegram.bot.command;

import com.dkarlov.dweather.telegram.bot.domain.Command;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
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

@Slf4j
public abstract class AbstractBotCommand extends BotCommand {
    private final Command command;

    public AbstractBotCommand(Command command) {
        super(command.name().toLowerCase(), command.getDescription());
        this.command = command;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            absSender.execute(processCommand(absSender, user, chat, arguments));
        } catch (TelegramApiException exception) {
            log.error("Error occurred while executing /" + command.name().toLowerCase() + " command", exception);
        }

    }

    protected InlineKeyboardMarkup createReplyKeyboard(List<List<Pair<String, String>>> buttons) {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for (List<Pair<String, String>> row : buttons) {
            final List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            for (Pair<String, String> button : row) {
                keyboardButtonsRow.add(new InlineKeyboardButton()
                        .setText(button.getValue())
                        .setCallbackData(button.getKey()));
            }
            rowList.add(keyboardButtonsRow);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    protected abstract SendMessage processCommand(AbsSender absSender, User user, Chat chat, String[] arguments);
}
