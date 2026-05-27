package com.example.persistence.examples.scheduler;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class NextExecutionCalculator {

    private final LocalTime start;
    private final LocalTime end;
    private final Duration delay;

    public NextExecutionCalculator(SchedulerProperties properties) {
        start = properties.getStartTime();
        end = properties.getEndTime();
        delay = Duration.ofMillis(properties.getDelayMs());
    }

    public LocalDateTime calculateNextRun(LocalDateTime now) {
        boolean overnightWindow = end.isBefore(start);
        LocalTime currentTime = now.toLocalTime();

        boolean insideWindow;

        if (overnightWindow) {
            // Example: 18:00 -> 08:00
            insideWindow = !currentTime.isBefore(start) || currentTime.isBefore(end);
        } else {
            insideWindow = !currentTime.isBefore(start) && currentTime.isBefore(end);
        }

        // Continue scheduling with delay
        if (insideWindow) {
            return now.plus(delay);
        }

        // Outside allowed interval -> wait until next start
        LocalDate nextDate = now.toLocalDate();

        if (currentTime.isAfter(start)) {
            nextDate = nextDate.plusDays(1);
        }

        return LocalDateTime.of(nextDate, start);
    }
}
