package edu.java.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.service.CommandService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

 class RegexTest {
    @Mock
    private TelegramBot bot;
    @InjectMocks
    private CommandService commandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testRegexFuncValidGitHubLink() {
        String validGitHubLink = "https://github.com/username/";
        boolean result = commandService.regexFunc(commandService.regexGitHub, validGitHubLink);
        assertTrue(result);
    }

    @Test
    void testRegexFuncInvalidGitHubLink() {
        String invalidGitHubLink = "https://invalidgithublink.com";
        boolean result = commandService.regexFunc(commandService.regexGitHub, invalidGitHubLink);
        assertFalse(result);
    }

    @Test
    void testRegexFuncValidStackOverflowLink() {
        String validStackOverflowLink = "https://stackoverflow.com/questions/12345678/example-question";
        boolean result = commandService.regexFunc(commandService.regexStackOverflow, validStackOverflowLink);
        assertTrue(result);
    }

    @Test
    void testRegexFuncInvalidStackOverflowLink() {
        String invalidStackOverflowLink = "https://invalidstackoverflowlink.com";
        boolean result = commandService.regexFunc(commandService.regexStackOverflow, invalidStackOverflowLink);
        assertFalse(result);
    }

    @Test
    void testRegexFuncWithValidGeneralLink() {
        String validGeneralLink = "https://example.com";
        boolean result = commandService.regexFunc(commandService.regexLinks, validGeneralLink);
        assertTrue(result);
    }
}
