package edu.java.scrapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.client.GitHubClient;
import edu.java.dto.GitHubResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class GitHubClientTest {

    private static WireMockServer wireMockServer;
    private static GitHubClient gitHubClient;

    @BeforeAll
    public static void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();

        String baseUrl = wireMockServer.baseUrl();

        gitHubClient = new GitHubClient(WebClient.builder().baseUrl(baseUrl).build());
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testGitHubClient_Success() {
        String owner = "testOwner";
        String repoName = "testRepo";

        // Configure stub for successful response
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/" + owner + "/" + repoName))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"name\": \"" + repoName + "\", \"updated_at\": \"2024-02-25T12:00:00Z\"}")));

        // Test GitHubClient
        Mono<GitHubResponse> responseMono = gitHubClient.getGitHubResponse(owner, repoName);

        StepVerifier.create(responseMono)
            .expectNextMatches(response -> response.getName().equals(repoName))
            .verifyComplete();
    }

    @Test
    public void testGitHubClient_Failure() {
        String owner = "testOwner";
        String repoName = "testRepo";

        // Configure stub for failure response
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/" + owner + "/" + repoName))
            .willReturn(WireMock.aResponse().withStatus(HttpStatus.NOT_FOUND.value())));

        // Test GitHubClient failure
        Mono<GitHubResponse> responseMono = gitHubClient.getGitHubResponse(owner, repoName);

        StepVerifier.create(responseMono)
            .expectError(WebClientResponseException.NotFound.class)
            .verify();
    }
}
