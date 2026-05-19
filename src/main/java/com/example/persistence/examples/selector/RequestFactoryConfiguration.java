package com.example.persistence.examples.selector;

import com.example.persistence.examples.config.ClientType;
import com.example.persistence.examples.selector.factory.AndroidRequestFactory;
import com.example.persistence.examples.selector.factory.IOSRequestFactory;
import com.example.persistence.examples.selector.factory.PartnerRequestFactory;
import com.example.persistence.examples.selector.factory.RequestFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@EnableConfigurationProperties(ClientsConfigurationProperties.class)
@Configuration
public class RequestFactoryConfiguration {

    @Bean
    public Map<ClientType, RequestFactory> requestFactories(
    ) {
        Map<ClientType, RequestFactory> map = new HashMap<>();

        map.put(ClientType.IOS, new IOSRequestFactory());
        map.put(ClientType.ANDROID, new AndroidRequestFactory());
        map.put(ClientType.PARTNER, new PartnerRequestFactory());
        return map;
    }

    @Bean
    public WeightedRequestFactorySelector weightedRequestFactorySelector(
            ClientsConfigurationProperties properties,
            Map<ClientType, RequestFactory> requestFactories
    ) {
        WeightedRequestFactorySelectorFactory factory = new WeightedRequestFactorySelectorFactory();

        return factory.create(properties.getClients(), requestFactories);
    }

}
