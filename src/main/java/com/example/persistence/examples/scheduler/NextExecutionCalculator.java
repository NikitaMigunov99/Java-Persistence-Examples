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
        LocalTime currentTime = now.toLocalTime();

        // Continue scheduling with delay
        if (isInsideWindow(currentTime)) {
            return now.plus(delay);
        }

        // Outside allowed interval -> wait until next start
        LocalDate nextDate = now.toLocalDate();

        if (currentTime.isAfter(start)) {
            nextDate = nextDate.plusDays(1);
        }

        return LocalDateTime.of(nextDate, start);
    }

    /**
     * Проверяет, находится ли заданное время внутри разрешённого временного окна.
     *
     * @return
     * start = 09:00, end = 18:00, currentTime = 10:30 → true  (внутри дневного окна)
     * start = 09:00, end = 18:00, currentTime = 20:00 → false (вне окна)
     * start = 18:00, end = 06:00, currentTime = 20:00 → true  (в вечерней части ночного окна)
     * start = 18:00, end = 06:00, currentTime = 05:00 → true  (в утренней части ночного окна)
     * start = 18:00, end = 06:00, currentTime = 06:00 → false (на границе — вне окна)
     *
     * @param time Время для проверки
     */
    public boolean isInsideWindow(LocalTime time) {
        boolean overnightWindow = end.isBefore(start);

        if (overnightWindow) {
            // Окно пересекает полночь: время должно быть >= start ИЛИ < end
            return !time.isBefore(start) || time.isBefore(end);
        } else {
            // Обычное окно: время должно быть >= start И < end
            return !time.isBefore(start) && time.isBefore(end);
        }
    }
}
