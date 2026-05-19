package com.example.persistence.examples.selector;

import com.example.persistence.examples.selector.factory.HeavyFactory;
import com.example.persistence.examples.selector.factory.RequestFactory;
import com.example.persistence.examples.selector.model.RequestDto;
import com.example.persistence.examples.selector.sender.AsyncRequestSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RequestOrchestratorTest {

    private final AsyncRequestSender sender = mock(AsyncRequestSender.class);
    private final WeightedRequestFactorySelector selector = mock(WeightedRequestFactorySelector.class);
    private final RequestFactory requestFactory = mock(RequestFactory.class);
    private final HeavyRequestFactory heavyRequestFactory = mock(HeavyRequestFactory.class);;

    private Executor heavyExecutor;

    @BeforeEach
    void setUp() {
        heavyExecutor = Executors.newSingleThreadExecutor();
    }

    @Test
    void runOrchestrator() throws Exception {
        when(selector.selectFactory()).thenReturn(requestFactory);
        when(requestFactory.createRequest()).thenReturn(new RequestDto("ios"));

        when(sender.send(any(RequestDto.class))).thenReturn(Mono.empty());

        RequestOrchestrator orchestrator =
                new RequestOrchestrator(
                        selector,
                        sender,
                        heavyExecutor,
                        3,
                        3
                );

        orchestrator.orchestrate();

        Thread.sleep(300);

        verify(selector, times(3)).selectFactory();
        verify(requestFactory, times(3)).createRequest();
        verify(sender, times(3)).send(any(RequestDto.class));
    }

    @Test
    void runWithRequestError() throws Exception {
        when(selector.selectFactory()).thenReturn(requestFactory);
        when(requestFactory.createRequest()).thenReturn(new RequestDto("ios"));

        when(sender.send(any(RequestDto.class))).thenReturn(Mono.error(new RuntimeException("Error during request")));

        RequestOrchestrator orchestrator =
                new RequestOrchestrator(
                        selector,
                        sender,
                        heavyExecutor,
                        3,
                        3
                );

        orchestrator.orchestrate();

        Thread.sleep(300);

        verify(selector, times(3)).selectFactory();
        verify(requestFactory, times(3)).createRequest();
        verify(sender, times(3)).send(any(RequestDto.class));
    }

    @Test
    void runWithHeavyFactory() throws Exception {
        when(selector.selectFactory()).thenReturn(heavyRequestFactory);
        when(heavyRequestFactory.createRequest()).thenReturn(new RequestDto("ios"));

        when(sender.send(any(RequestDto.class))).thenReturn(Mono.empty());

        RequestOrchestrator orchestrator =
                new RequestOrchestrator(
                        selector,
                        sender,
                        heavyExecutor,
                        3,
                        3
                );

        orchestrator.orchestrate();

        Thread.sleep(300);

        verify(selector, times(3)).selectFactory();
        verify(heavyRequestFactory, times(3)).createRequest();
        verify(sender, times(3)).send(any(RequestDto.class));
    }

    static class HeavyRequestFactory implements HeavyFactory, RequestFactory {

        @Override
        public RequestDto createRequest() {
            return new RequestDto("heavy factory");
        }
    }
}
