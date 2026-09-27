package com.vladislav.habittrackerimpl.service;

import com.vladislav.entity.Record;
import com.vladislav.exception.HabitNotFoundException;
import com.vladislav.repository.HabitRepository;
import com.vladislav.repository.RecordRepository;
import com.vladislav.service.impl.StatsServiceImpl;
import com.vladislav.util.StreakCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.vladislav.habittrackerimpl.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private StreakCalculator streakCalculator;

    @InjectMocks
    private StatsServiceImpl statsService;

    @Test
    void shouldCalculateCompletionRateCorrectly() {
        var user = getUserForUnit();
        LocalDate today = LocalDate.now();
        var habit = getHabitWithIdForUnit();
        ReflectionTestUtils.setField(habit, "createdAt", today.minusDays(6).atStartOfDay());

        when(habitRepository.findByIdAndUserId(habit.getId(),user.getId())).thenReturn(Optional.of(habit));
        when(recordRepository.findByHabitIdOrderByDateDesc(habit.getId())).thenReturn(List.of(new Record(), new Record(), new Record()));
        when(streakCalculator.currentStreak(anyList())).thenReturn(3);
        when(streakCalculator.bestStreak(anyList())).thenReturn(3);
        when(recordRepository.countByHabitId(habit.getId())).thenReturn(3);
        when(recordRepository.countByHabitIdAndDateBetween(habit.getId(), today.minusDays(6), today)).thenReturn(3);

        var result = statsService.getHabitStats(habit.getId(), user);

        assertEquals(42.9, result.getCompletionRate());
        assertEquals(3, result.getTotalCompletions());
        assertEquals(3, result.getCurrentStreak());
        assertEquals(3, result.getBestStreak());
    }

    @Test
    void shouldThrowWhenHabitNotFoundForStats() {
        var user = getUserForUnit();
        var habit = getHabitWithIdForUnit();

        when(habitRepository.findByIdAndUserId(habit.getId(), user.getId())).thenReturn(Optional.empty());

        assertThrows(HabitNotFoundException.class, () -> statsService.getHabitStats(habit.getId(), user));
    }

    @Test
    void shouldReturnDailyStatsOnlyForOwnHabits() {
        var user = getUserForUnit();
        var habit = getHabitWithIdForUnit();

        var todayRecord = getRecordForUnit();
        todayRecord.setHabit(habit);

        when(recordRepository.findByDate(LocalDate.now())).thenReturn(List.of(todayRecord));
        when(habitRepository.findAllByUserId(user.getId())).thenReturn(List.of(habit));

        var result = statsService.getHabitDailyStats(user);

        assertEquals(1, result.getHabits().size());
        assertTrue(result.getHabits().getFirst().getCompleted());
    }

    @Test
    void shouldReturnWeeklyStatsWithCorrectTotalCountForOwner() {
        var user = getUserForUnit();
        LocalDate today = LocalDate.now();
        LocalDate lastDay = today.minusDays(6);

        when(habitRepository.countByUserId(user.getId())).thenReturn(3L);
        when(recordRepository.findByUserIdAndDateBetween(user.getId(), lastDay, today)).thenReturn(List.of());

        var result = statsService.getWeeklyHabitStats(user);

        assertEquals(7, result.getWeek().size());
        result.getWeek().forEach(day -> assertEquals(3L, day.getTotalCount()));
    }
}
