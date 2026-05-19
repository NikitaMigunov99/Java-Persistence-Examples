package com.example.persistence.examples.selector;

import com.example.persistence.examples.selector.factory.HeavyFactory;
import com.example.persistence.examples.selector.factory.RequestFactory;
import com.example.persistence.examples.selector.model.RequestDto;
import com.example.persistence.examples.selector.sender.AsyncRequestSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j
@Component
public class RequestOrchestrator {

    /**
     * Number of requests generated
     * during one scheduler iteration.
     * <p>
     * Since scheduler runs every second,
     * this is effectively requests/sec.
     */
    private static final int REQUESTS_PER_SECOND = 500;

    /**
     * Max simultaneous in-flight requests.
     */
    private static final int MAX_IN_FLIGHT = 2_000;

    /**
     * Small delay when max in-flight reached.
     */
    private static final long BACKPRESSURE_DELAY_MS = 10;

    private final WeightedRequestFactorySelector selector;

    private final AsyncRequestSender sender;

    /**
     * Used only for heavy/blocking factories.
     */
    private final Executor heavyExecutor;

    public RequestOrchestrator(WeightedRequestFactorySelector selector,
                               AsyncRequestSender sender,
                               @Qualifier("heavyExecutor")
                               Executor heavyExecutor) {
        this.selector = selector;
        this.sender = sender;
        this.heavyExecutor = heavyExecutor;
    }

    /**
     * Limits simultaneous in-flight requests.
     */
    private final Semaphore inFlightSemaphore = new Semaphore(MAX_IN_FLIGHT);

    /**
     * Runs once per second.
     */
    @Scheduled(fixedRate = 1_000)
    public void orchestrate() {
        for (int i = 0; i < REQUESTS_PER_SECOND; i++) {
            trySendRequest();
        }
    }

    private void trySendRequest() {
        if (!inFlightSemaphore.tryAcquire()) {
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(BACKPRESSURE_DELAY_MS));
        }
        RequestFactory factory = selector.selectFactory();

        if (factory instanceof HeavyFactory) {
            CompletableFuture
                    .supplyAsync(factory::createRequest, heavyExecutor)
                    .thenCompose(this::sendAsync)
                    .whenComplete(this::handleCompletion);
            return;
        }

        try {
            RequestDto dto = factory.createRequest();
            sendAsync(dto).whenComplete(this::handleCompletion);
        } catch (Exception ex) {
            handleCompletion(null, ex);
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
