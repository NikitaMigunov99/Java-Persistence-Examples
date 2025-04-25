package com.example.persistence.examples.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.LoggingCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfiguration implements CachingConfigurer {

    @Override
    public CacheErrorHandler errorHandler() {
        return new LoggingCacheErrorHandler();
    }

    @Bean(destroyMethod = "shutdown")
    public ClientResources redisClientResources() {
        return DefaultClientResources.create();
    }

    @Bean
    public ClientOptions redisClientOptions() {
        return ClientOptions.builder()
                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                .autoReconnect(true).build();
    }

    @Bean
    public LettucePoolingClientConfiguration lettucePoolingClientConfiguration(
            ClientOptions redisClientOptions,
            ClientResources redisClientResources,
            RedisProperties redisProperties) {
        return LettucePoolingClientConfiguration.builder()
                .commandTimeout(redisProperties.getReadTimeout())
                .poolConfig(redisProperties.getPoolConfig())
                .clientOptions(redisClientOptions)
                .clientResources(redisClientResources).build();
    }

    @Bean
    public RedisClusterConfiguration customRedisCluster(RedisProperties redisProperties) {
        System.out.println("RedisProperties: " + redisProperties);
        RedisClusterConfiguration configuration = new RedisClusterConfiguration(redisProperties.getNodes());
        configuration.setUsername(redisProperties.getUsername());
        configuration.setPassword(redisProperties.getPassword());
        return new RedisClusterConfiguration(redisProperties.getNodes());
    }

    @Bean
    public RedisConnectionFactory lettuceConnectionFactory(
            RedisClusterConfiguration customRedisCluster,
            LettucePoolingClientConfiguration lettucePoolingClientConfiguration) {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(customRedisCluster,
                lettucePoolingClientConfiguration);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(5))
                .serializeKeysWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}
