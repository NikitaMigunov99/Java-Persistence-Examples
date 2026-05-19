package com.example.persistence.examples.selector;

import com.example.persistence.examples.config.ClientProperties;
import com.example.persistence.examples.config.ClientType;
import com.example.persistence.examples.selector.factory.RequestFactory;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class WeightedRequestFactorySelectorTest {

    @Test
    void shouldSelectFactoryAccordingToWeight() {

        RequestFactory iosFactory = mock(RequestFactory.class);
        RequestFactory androidFactory = mock(RequestFactory.class);
        RequestFactory partnerFactory = mock(RequestFactory.class);

        Map<ClientType, RequestFactory> factories = new HashMap<>();

        factories.put(ClientType.IOS, iosFactory);
        factories.put(ClientType.ANDROID, androidFactory);
        factories.put(ClientType.PARTNER, partnerFactory);

        List<ClientProperties> properties =
                List.of(

                        createProperties(
                                "ios",
                                "IOS",
                                10_000_000L,
                                ChronoUnit.DAYS
                        ),

                        createProperties(
                                "android",
                                "ANDROID",
                                5_000_000L,
                                ChronoUnit.DAYS
                        ),

                        createProperties(
                                "partner",
                                "PARTNER",
                                10L,
                                ChronoUnit.HOURS
                        )
                );

        WeightedRequestFactorySelectorFactory factory = new WeightedRequestFactorySelectorFactory();

        WeightedRequestFactorySelector selector = factory.create(properties, factories);

        AtomicInteger iosCount = new AtomicInteger();
        AtomicInteger androidCount = new AtomicInteger();
        AtomicInteger partnerCount = new AtomicInteger();

        int iterations = 100_000;

        for (int i = 0; i < iterations; i++) {
            RequestFactory selected = selector.selectFactory();
            if (selected.equals(iosFactory)) {
                iosCount.incrementAndGet();
            } else if (selected.equals(androidFactory)) {
                androidCount.incrementAndGet();
            } else {
                partnerCount.incrementAndGet();
            }
        }

        System.out.println("ios=" + iosCount.get());
        System.out.println("android=" + androidCount.get());
        System.out.println("partner=" + partnerCount.get());

        assertTrue(iosCount.get() > androidCount.get());
        assertTrue(androidCount.get() > partnerCount.get());
        assertTrue(iosCount.get() > 60_000);
        assertTrue(partnerCount.get() < 100);
    }

    private ClientProperties createProperties(
            String name,
            String code,
            Long requestNumber,
            ChronoUnit unit
    ) {

        ClientProperties properties = new ClientProperties();
        properties.setName(name);
        properties.setCode(code);
        properties.setRequestNumber(requestNumber);
        properties.setRequestNumberUnit(unit);
        return properties;
    }
}