package edu.java.client;

import edu.java.dto.StackOverflowResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class StackOverflowClient {
    private final WebClient stackOverflowWebClient;

    public Mono<StackOverflowResponse> getStackOverflowResponse(long id) {
        return stackOverflowWebClient.get()
            .uri("/questions/{id}?site=stackoverflow", id)
            .retrieve()
            .bodyToMono(StackOverflowResponse.class);
    }
}

