package com.example.persistence.examples.config;

import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.List;

@Data
@Validated
@ConfigurationProperties(prefix = "redis.config")
public class RedisProperties {
    private final String username;
    private final String password;
    private final Duration readTimeout;
    private final List<String> nodes;
    private final int maxTotalPool;
    private final int maxIdlePool;
    private final int minIdlePool;
    private final Duration maxWait;

    public <T> GenericObjectPoolConfig<T> getPoolConfig() {
        GenericObjectPoolConfig<T> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(maxTotalPool);
        config.setMaxIdle(maxIdlePool);
        config.setMinIdle(minIdlePool);
        config.setMaxWait(maxWait);
        return config;
    }
}
