package edu.java.client;

import edu.java.dto.GitHubResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GitHubClient {
    private final WebClient githubWebClient;

    public Mono<GitHubResponse> getGitHubResponse(String owner, String name) {
        return githubWebClient.get()
            .uri("/repos/{owner}/{name}", owner, name)
            .retrieve()
            .bodyToMono(GitHubResponse.class);
    }
}
