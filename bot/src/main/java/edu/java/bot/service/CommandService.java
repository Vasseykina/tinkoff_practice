package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static edu.java.bot.utils.Commands.HELP;
import static edu.java.bot.utils.Commands.LIST;
import static edu.java.bot.utils.Commands.START;
import static edu.java.bot.utils.Commands.TRACK;
import static edu.java.bot.utils.Commands.UNTRACK;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandService {
    private final TelegramBot bot;

    private HashMap<Long, List<String>> links = new HashMap<>();
    private HashMap<Long, String> commands = new HashMap<>();

    private final String regexGitHub = "(https?://)?(www\\.)?github\\.com/\\S+";
    private final String regexLinks = "(https?://)?(www\\.)?\\S+\\.\\S+";
    String regexStackOverflow = "(https?://)?(www\\.)?stackoverflow\\.com/\\S+";
    private final String helpMessage = """
        /start -- зарегистрировать пользователя
        /help -- вывести окно с командами
        /track -- начать отслеживание ссылки
        /untrack -- прекратить отслеживание ссылки
        /list -- показать список отслеживаемых ссылок
        """;

    private void logCommand(String command, Long chatId) {
        log.info("User " + chatId + " send command " + command);
    }

    public void botAnswer(Long chatId, String answer) {
        bot.execute(new SendMessage(chatId, answer));
    }

    public void unknownCommand(Long chatId) {
        botAnswer(chatId, "Unknown command. See all commands /help.");
    }

    public boolean regexFunc(String regex, String text) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public void start(Long chatId) {
        botAnswer(chatId, "Hi! Welcome to bot\n");
        links.put(chatId, new ArrayList<>());
        logCommand(START, chatId);
    }

    public void help(Long chatId) {
        botAnswer(chatId, helpMessage);
        logCommand(HELP, chatId);
    }

    public void track(Long chatId) {
        botAnswer(chatId, "Send link for track\n");
        commands.put(chatId, TRACK);
        logCommand(TRACK, chatId);
    }

    public void untrack(Long chatId) {
        var userLinks = links.get(chatId);
        if (userLinks == null) {
            botAnswer(chatId, "Your list is empty");
            return;
        }
        if (userLinks.isEmpty()) {
            botAnswer(chatId, "You don't have any links to delete\n Add links - " + TRACK);
        } else {
            StringBuilder msg = new StringBuilder("Available links to delete\n");
            for (String link : userLinks) {
                msg.append(link).append("\n");
            }
            botAnswer(chatId, msg.toString());
            botAnswer(chatId, "Send link for untrack\n");
            commands.put(chatId, UNTRACK);
        }
        logCommand(UNTRACK, chatId);
    }

    public void list(Long chatId) {
        logCommand(LIST, chatId);
        var userLinks = links.get(chatId);
        if (userLinks == null) {
            botAnswer(chatId, "List doesn't exist");
            return;
        }
        if (userLinks.isEmpty()) {
            botAnswer(chatId, "List is empty. To track links - /track ");
        } else {
            StringBuilder msg = new StringBuilder("Your added links: \n");
            for (String link : userLinks) {
                msg.append(link).append("\n");
            }
            botAnswer(chatId, msg.toString());
        }
    }

    public void noCommand(Long chatId, String text) {
        if (commands.containsKey(chatId)) {
            switch (commands.get(chatId)) {
                case TRACK -> {
                    if (regexFunc(regexGitHub, text) || regexFunc(regexStackOverflow, text)) {
                        links.get(chatId).add(text);
                        commands.remove(chatId);
                        botAnswer(chatId, text + " added for your list.\n Tab /list to see your links");
                        log.info(text);
                    } else if (regexFunc(regexLinks, text)) {
                        botAnswer(chatId, "Link isn't correct.\n "
                            + "Try to use link only for GitHub or StackOverflow.\n Add links - "
                            + TRACK);
                        log.info(text);
                    } else {
                        unknownCommand(chatId);
                        log.info(text);
                    }
                }
                case UNTRACK -> {
                    if (links.get(chatId).contains(text)) {
                        links.get(chatId).remove(text);
                        commands.remove(chatId);
                        botAnswer(chatId, text + " has been removed. Check your links using " + LIST);
                    } else {
                        botAnswer(
                            chatId,
                            "The link you provided is not found in your list.\nUse "
                                + LIST
                                + " to see your added links."
                        );
                    }
                }
                default -> log.error("unknown input");
            }
        } else {
            unknownCommand(chatId);
        }
    }

}
