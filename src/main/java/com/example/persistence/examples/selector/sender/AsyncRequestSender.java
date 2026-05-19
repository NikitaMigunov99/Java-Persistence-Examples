package com.example.persistence.examples.selector.sender;

import com.example.persistence.examples.selector.model.RequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncRequestSender {

    private final WebClient webClient;

    public Mono<Void> send(RequestDto dto) {
        return webClient.post()
                .uri("/random_joke")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(ex -> log.error("Request failed", ex))
                .onErrorResume(ex -> Mono.empty());
    }
}
