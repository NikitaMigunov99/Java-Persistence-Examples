package com.example.persistence.examples.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.example.persistence.examples")
@ComponentScan(basePackages = "com.example.persistence.examples")
@EnableCaching
public class AppConfiguration {
}
