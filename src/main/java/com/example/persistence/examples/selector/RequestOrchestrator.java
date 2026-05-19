package com.example.persistence.examples.selector;

import com.example.persistence.examples.selector.factory.HeavyFactory;
import com.example.persistence.examples.selector.factory.RequestFactory;
import com.example.persistence.examples.selector.model.RequestDto;
import com.example.persistence.examples.selector.sender.AsyncRequestSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j
@Component
public class RequestOrchestrator {

    /**
     * Small delay when max in-flight reached.
     */
    private static final long BACKPRESSURE_DELAY_MS = 10;


    private final WeightedRequestFactorySelector selector;
    private final AsyncRequestSender sender;
    private final Semaphore inFlightSemaphore;
    private final int requestsPerSecond;


    /**
     * Used only for heavy/blocking factories.
     */
    private final Executor heavyExecutor;

    public RequestOrchestrator(WeightedRequestFactorySelector selector,
                               AsyncRequestSender sender,
                               @Qualifier("heavyExecutor")
                               Executor heavyExecutor,
                               int requestsPerSecond,
                               int maxRequestsInFlight) {
        this.selector = selector;
        this.sender = sender;
        this.heavyExecutor = heavyExecutor;
        this.inFlightSemaphore = new Semaphore(maxRequestsInFlight);
        this.requestsPerSecond = requestsPerSecond;
    }

    /**
     * Runs once per second.
     */
    @Scheduled(fixedRate = 1_000)
    public void orchestrate() {
        CompletableFuture<?>[] futures = new CompletableFuture[requestsPerSecond];
        for (int i = 0; i < requestsPerSecond; i++) {
            futures[i] = trySendRequest();
        }
        CompletableFuture.allOf(futures).exceptionally((e -> null)).join();
    }

    private CompletableFuture<Void> trySendRequest() {
        while (!inFlightSemaphore.tryAcquire()) {
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(BACKPRESSURE_DELAY_MS));
        }
        RequestFactory factory = selector.selectFactory();

        if (factory instanceof HeavyFactory) {
           return CompletableFuture
                    .supplyAsync(factory::createRequest, heavyExecutor)
                    .thenCompose(this::sendAsync)
                    .whenComplete(this::handleCompletion);
        }

        try {
            RequestDto dto = factory.createRequest();
            return sendAsync(dto).whenComplete(this::handleCompletion);
        } catch (Exception ex) {
            handleCompletion(null, ex);
            return CompletableFuture.failedFuture(ex);
        }
    }

    private CompletableFuture<Void> sendAsync(RequestDto dto) {
        return sender.send(dto).toFuture();
    }

    private void handleCompletion(Void unused, Throwable ex) {
        inFlightSemaphore.release();
        if (ex != null) {
            log.error("Request failed", ex);
        }
    }
}
