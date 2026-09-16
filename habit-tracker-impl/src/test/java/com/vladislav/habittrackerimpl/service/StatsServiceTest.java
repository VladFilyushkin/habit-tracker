package com.vladislav.habittrackerimpl.service;

import com.vladislav.dto.response.HabitDailyStatusRs;
import com.vladislav.entity.Habit;
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
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        var randomId = RandomGenerator.getDefault().nextLong();
        LocalDate today = LocalDate.now();

        var habit = new Habit();
        ReflectionTestUtils.setField(habit, "id", randomId);
        ReflectionTestUtils.setField(habit, "createdAt", today.minusDays(6).atStartOfDay());

        when(habitRepository.findById(randomId)).thenReturn(Optional.of(habit));
        when(recordRepository.findByHabitIdOrderByDateDesc(randomId)).thenReturn(List.of(new Record(), new Record(), new Record()));
        when(streakCalculator.currentStreak(anyList())).thenReturn(3);
        when(streakCalculator.bestStreak(anyList())).thenReturn(3);
        when(recordRepository.countByHabitId(randomId)).thenReturn(3);
        when(recordRepository.countByHabitIdAndDateBetween(randomId, today.minusDays(6), today)).thenReturn(3);

        var result = statsService.getHabitStats(randomId);

        assertEquals(42.9, result.getCompletionRate());
        assertEquals(3, result.getTotalCompletions());
        assertEquals(3, result.getCurrentStreak());
        assertEquals(3, result.getBestStreak());
    }

    @Test
    void shouldThrowWhenHabitNotFoundForStats() {
        var randomId = RandomGenerator.getDefault().nextLong();
        when(habitRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(HabitNotFoundException.class, () -> statsService.getHabitStats(randomId));
    }

    @Test
    void shouldMarkOnlyTodayCompletedHabitsAsCompletedInDailyStats() {
        var completedHabit = new Habit();
        ReflectionTestUtils.setField(completedHabit, "id", 1L);
        completedHabit.setName("drink water");

        var notCompletedHabit = new Habit();
        ReflectionTestUtils.setField(notCompletedHabit, "id", 2L);
        notCompletedHabit.setName("jog");

        var todayRecord = new Record();
        todayRecord.setHabit(completedHabit);

        when(recordRepository.findByDate(LocalDate.now())).thenReturn(List.of(todayRecord));
        when(habitRepository.findAll()).thenReturn(List.of(completedHabit, notCompletedHabit));

        var result = statsService.getHabitDailyStats();

        var statusById = result.getHabits().stream()
                .collect(java.util.stream.Collectors.toMap(HabitDailyStatusRs::getId, HabitDailyStatusRs::getCompleted));

        assertTrue(statusById.get(1L));
        assertFalse(statusById.get(2L));
    }

    @Test
    void shouldReturnTotalCountEqualToAllHabitsRegardlessOfCompletion() {
        when(recordRepository.findByDateBetween(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
                .thenReturn(List.of());
        when(habitRepository.count()).thenReturn(5L);

        var result = statsService.getWeeklyHabitStats();

        assertEquals(7, result.getWeek().size());
        result.getWeek().forEach(day -> assertEquals(5L, day.getTotalCount()));
    }
}
