package com.dkarlov.dweather.telegram.bot.command.processor;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractCommandResponseProcessor {

    public AnswerCallbackQuery processCallbackResponse(Update update) {
        return new AnswerCallbackQuery()
                .setCallbackQueryId(update.getCallbackQuery().getId())
                .setText(process(update));
    }

    protected abstract String process(Update update);
}
