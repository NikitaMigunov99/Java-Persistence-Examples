package com.example.persistence.examples.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Slf4j
@Configuration
@EnableScheduling
@EnableConfigurationProperties(SchedulerProperties.class)
public class DynamicSchedulerConfig implements SchedulingConfigurer {

    private final NextExecutionCalculator nextExecutionCalculator;

    public DynamicSchedulerConfig(NextExecutionCalculator nextExecutionCalculator) {
        this.nextExecutionCalculator = nextExecutionCalculator;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                this::executeTask,
                triggerContext -> {
                    log.info("calculateNextRun at {}", LocalTime.now());

                    LocalDateTime now = LocalDateTime.now();
                    if (triggerContext.lastCompletion() == null
                            && nextExecutionCalculator.isInsideWindow(now.toLocalTime())) {
                        return Instant.now();
                    }

                    LocalDateTime nextRun = nextExecutionCalculator.calculateNextRun(now);
                    return nextRun
                            .atZone(ZoneId.systemDefault())
                            .toInstant();
                }
        );
    }

    private void executeTask() {
        log.info("Executing scheduled task at {}", LocalTime.now());

        // BUSINESS LOGIC
        log.info("Finished scheduled task at {}", LocalTime.now());
    }

}