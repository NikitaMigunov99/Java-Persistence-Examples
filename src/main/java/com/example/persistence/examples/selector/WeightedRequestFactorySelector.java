package com.example.persistence.examples.selector;

import com.example.persistence.examples.selector.factory.RequestFactory;

import java.util.NavigableMap;
import java.util.concurrent.ThreadLocalRandom;

public class WeightedRequestFactorySelector {

    private final NavigableMap<Long, RequestFactory> distribution;
    private final long totalWeight;

    public WeightedRequestFactorySelector(
            NavigableMap<Long, RequestFactory> distribution,
            long totalWeight
    ) {
        this.distribution = distribution;
        this.totalWeight = totalWeight;
    }

    public RequestFactory selectFactory() {
        if (distribution.isEmpty()) {
            throw new IllegalStateException("No factories configured");
        }
        long random = ThreadLocalRandom.current().nextLong(totalWeight) + 1;

        return distribution.ceilingEntry(random).getValue();
    }
}
