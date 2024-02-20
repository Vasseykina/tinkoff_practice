package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.CommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

class CommandServiceTest {

    @Mock
    private TelegramBot bot;

    @InjectMocks
    private CommandService commandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHelpCommand() {
        long chatId = 123456789;
        commandService.help(chatId);
        verify(bot).execute(any(SendMessage.class));
    }

    @Test
    void testTrackCommand() {
        long chatId = 123456789;
        commandService.track(chatId);
        verify(bot).execute(any(SendMessage.class));
    }

    @Test
    void testUnknownCommand() {
        long chatId = 123456789;
        String text = "/unknown_command";
        commandService.noCommand(chatId, text);
        verify(bot).execute(any(SendMessage.class));
    }

    @Test
    void testListCommand() {
        long chatId = 123456789;
        commandService.list(chatId);
        verify(bot).execute(any(SendMessage.class));
    }

}
