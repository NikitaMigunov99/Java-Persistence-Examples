package com.example.persistence.examples.selector;

import com.example.persistence.examples.config.ClientProperties;
import com.example.persistence.examples.config.ClientType;
import com.example.persistence.examples.selector.factory.RequestFactory;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WeightedRequestFactorySelectorFactory {

    public WeightedRequestFactorySelector create(
            List<ClientProperties> properties,
            Map<ClientType, RequestFactory> factories
    ) {

        NavigableMap<Long, RequestFactory> distribution =
                new TreeMap<>();

        long cumulative = 0;

        Map<String, ClientProperties> propertiesByCode =
                properties.stream()
                        .collect(Collectors.toMap(
                                ClientProperties::getCode,
                                Function.identity()
                        ));

        for (Map.Entry<ClientType, RequestFactory> entry : factories.entrySet()) {

            ClientType clientType = entry.getKey();
            RequestFactory factory = entry.getValue();

            ClientProperties props =
                    propertiesByCode.get(clientType.name());

            if (props == null) {
                continue;
            }

            long weight = normalizeWeight(
                    props.getRequestNumber(),
                    props.getRequestNumberUnit()
            );

            if (weight <= 0) {
                continue;
            }

            cumulative += weight;

            distribution.put(cumulative, factory);
        }

        return new WeightedRequestFactorySelector(
                distribution,
                cumulative
        );
    }

    private long normalizeWeight(
            Long requestNumber,
            ChronoUnit unit
    ) {
        long seconds = unit.getDuration().getSeconds();
        if (seconds <= 0) {
            return requestNumber;
        }
        return requestNumber * (86400 / seconds);
    }
}