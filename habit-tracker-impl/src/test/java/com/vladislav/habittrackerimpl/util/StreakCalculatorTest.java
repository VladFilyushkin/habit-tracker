package com.vladislav.habittrackerimpl.util;

import com.vladislav.util.StreakCalculator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StreakCalculatorTest {

    private final StreakCalculator calculator = new StreakCalculator();

    @Test
    void shouldCalculateCurrentStreakForConsecutiveDays() {
        LocalDate today = LocalDate.now();
        var dates = List.of(today, today.minusDays(1), today.minusDays(2));

        assertEquals(3, calculator.currentStreak(dates));
    }

    @Test
    void shouldBreakCurrentStreakWhenTodayIsMissing() {
        LocalDate today = LocalDate.now();
        var dates = List.of(today.minusDays(1), today.minusDays(2));

        assertEquals(0, calculator.currentStreak(dates));
    }

    @Test
    void shouldStopCurrentStreakAtFirstGap() {
        LocalDate today = LocalDate.now();
        var dates = List.of(today, today.minusDays(1), today.minusDays(3));

        assertEquals(2, calculator.currentStreak(dates));
    }

    @Test
    void shouldFindBestStreakAcrossHistoryWithGap() {
        LocalDate start = LocalDate.of(2026, 4, 14);
        var dates = List.of(
                start, start.plusDays(1), start.plusDays(2),
                start.plusDays(4), start.plusDays(5), start.plusDays(6)
        );

        assertEquals(3, calculator.bestStreak(dates));
    }

    @Test
    void shouldReturnZeroBestStreakForEmptyList() {
        assertEquals(0, calculator.bestStreak(List.of()));
    }
}
