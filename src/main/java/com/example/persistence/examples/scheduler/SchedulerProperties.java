package com.example.persistence.examples.scheduler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalTime;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.scheduler")
public class SchedulerProperties {

    /**
     * Example: 18:00
     */
    private LocalTime startTime;

    /**
     * Example: 08:00
     */
    private LocalTime endTime;

    /**
     * Delay between calls
     */
    private long delayMs;
}
