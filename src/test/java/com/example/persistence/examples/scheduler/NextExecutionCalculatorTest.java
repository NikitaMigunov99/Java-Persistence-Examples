package com.example.persistence.examples.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NextExecutionCalculatorTest {

    private final SchedulerProperties properties = mock(SchedulerProperties.class);

    private NextExecutionCalculator calculator;

    @BeforeEach
    void setUp() {
        when(properties.getStartTime()).thenReturn(LocalTime.of(18,0));
        when(properties.getEndTime()).thenReturn(LocalTime.of(8, 0));
        when(properties.getDelayMs()).thenReturn(300_000L); // 5 minutes
        calculator = new NextExecutionCalculator(properties);
    }

    @Test
    void shouldAddDelayWhenInsideEveningWindow() {
        LocalDateTime now = LocalDateTime.of(2026, 5, 27, 19, 0);

        LocalDateTime result = calculator.calculateNextRun(now);
        assertThat(result).isEqualTo(now.plusMinutes(5));
    }

    @Test
    void shouldAddDelayWhenInsideNightWindow() {
        LocalDateTime now = LocalDateTime.of(2026, 5, 28, 2, 0);

        LocalDateTime result = calculator.calculateNextRun(now);

        assertThat(result).isEqualTo(now.plusMinutes(5));
    }

    @Test
    void shouldScheduleAtStartTimeWhenOutsideWindowBeforeStart() {
        LocalDateTime now = LocalDateTime.of(2026, 5, 27, 12, 0);

        LocalDateTime result = calculator.calculateNextRun(now);
        assertThat(result).isEqualTo(LocalDateTime.of(2026, 5, 27, 18, 0));
    }

    @Test
    void shouldScheduleNextDayWhenOutsideWindowAfterEnd() {
        LocalDateTime now = LocalDateTime.of(2026, 5, 27, 10, 0);

        LocalDateTime result = calculator.calculateNextRun(now);
        assertThat(result).isEqualTo(LocalDateTime.of(2026, 5, 27, 18, 0));
    }

    @Test
    void shouldScheduleNextDayWhenAfterStartButOutsideWindow() {
        when(properties.getStartTime()).thenReturn(LocalTime.of(8, 0));
        when(properties.getEndTime()).thenReturn(LocalTime.of(18, 0));

        LocalDateTime now = LocalDateTime.of(2026, 5, 27, 20, 0);

        LocalDateTime result = calculator.calculateNextRun(now);
        assertThat(result).isEqualTo(LocalDateTime.of(2026, 5, 28, 8, 0));
    }

    @Test
    void shouldHandleExactStartTime() {
        LocalDateTime now = LocalDateTime.of(2026, 5, 27, 18, 0);

        LocalDateTime result = calculator.calculateNextRun(now);
        assertThat(result).isEqualTo(now.plusMinutes(5));
    }

    @Test
    void shouldHandleExactEndTimeAsOutsideWindow() {
        LocalDateTime now = LocalDateTime.of(2026, 5, 28, 8, 0);

        LocalDateTime result = calculator.calculateNextRun(now);
        assertThat(result).isEqualTo(LocalDateTime.of(2026, 5, 28, 18, 0));
    }
}
