package edu.java.bot.controllers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import edu.java.bot.service.CommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import static edu.java.bot.utils.Commands.HELP;
import static edu.java.bot.utils.Commands.LIST;
import static edu.java.bot.utils.Commands.START;
import static edu.java.bot.utils.Commands.TRACK;
import static edu.java.bot.utils.Commands.UNTRACK;

@Controller
@Slf4j
public class TelegramBotStruct {

    TelegramBotStruct(TelegramBot bot, CommandService commandService) {
        bot.setUpdatesListener(
            updates -> {
                log.info("updates" + updates);
                var msg = updates.get(0).message();
                long chatId = updates.get(0).message().chat().id();
                switch (updates.get(0).message().text()) {
                    case START -> commandService.start(chatId);
                    case HELP -> commandService.help(chatId);
                    case TRACK -> commandService.track(chatId);
                    case UNTRACK -> commandService.untrack(chatId);
                    case LIST -> commandService.list(chatId);
                    default -> commandService.noCommand(chatId, msg.text());
                }
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            },
            e -> {
                if (e.response() != null) {
                    e.response().errorCode();
                    e.response().description();
                } else {
                    log.error(e.getMessage());
                }
            }
        );
    }

}
