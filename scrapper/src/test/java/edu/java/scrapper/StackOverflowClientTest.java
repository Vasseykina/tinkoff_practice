package edu.java.scrapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.client.StackOverflowClient;
import edu.java.dto.StackOverflowResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.OffsetDateTime;

public class StackOverflowClientTest {

    private static WireMockServer wireMockServer;
    private static StackOverflowClient stackOverflowClient;

    @BeforeAll
    public static void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
        String baseUrl = wireMockServer.baseUrl();
        stackOverflowClient = new StackOverflowClient(WebClient.builder().baseUrl(baseUrl).build());
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testGetGitHubResponse() {
        // Настройка заглушки для запроса
        long questionId = 12345;
        String title = "Sample Question";
        OffsetDateTime lastActivityDate = OffsetDateTime.now();

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/questions/" + questionId))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"title\": \"" + title + "\", \"last_activity_date\": \"" + lastActivityDate.toString() + "\"}")));

        // Вызов метода и проверка ответа
        Mono<StackOverflowResponse> responseMono = stackOverflowClient.getStackOverflowResponse(questionId);
        responseMono.subscribe(response -> {
            // Проверка данных
            assert response.getTitle().equals(title);
            assert response.getLastActivityDate().equals(lastActivityDate);
        });
    }
}
