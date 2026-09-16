package com.vladislav.service.impl;

import com.vladislav.dto.response.*;
import com.vladislav.entity.Habit;
import com.vladislav.entity.Record;
import com.vladislav.exception.HabitNotFoundException;
import com.vladislav.repository.HabitRepository;
import com.vladislav.repository.RecordRepository;
import com.vladislav.service.StatsService;
import com.vladislav.util.StreakCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.vladislav.constant.MessageConstant.HABIT_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final HabitRepository habitRepository;
    private final RecordRepository recordRepository;
    private final StreakCalculator streakCalculator;

    @Override
    @Transactional(readOnly = true)
    public HabitStatsRs getHabitStats(Long habitId) {
        Habit habit = habitRepository.findById(habitId).orElseThrow(() ->
                new HabitNotFoundException(String.format(HABIT_NOT_FOUND_EXCEPTION, habitId)));

        List<Record> recordList = recordRepository.findByHabitIdOrderByDateDesc(habitId);
        List<LocalDate> descendingList = recordList.stream().map(Record::getDate).toList();
        List<LocalDate> ascendingList = descendingList.reversed();

        int currentStreak = streakCalculator.currentStreak(descendingList);
        int bestStreak = streakCalculator.bestStreak(ascendingList);
        int completedDays = recordRepository.countByHabitId(habitId);

        LocalDate creationDate = habit.getCreatedAt().toLocalDate();
        LocalDate today = LocalDate.now();

        int completedInPeriod = recordRepository.countByHabitIdAndDateBetween(habitId, creationDate, today);
        long daysSinceCreation = ChronoUnit.DAYS.between(creationDate, today) + 1;

        double completionRate = 0.0;
        if (daysSinceCreation > 0) {
            completionRate = Math.round((completedInPeriod * 1000.0) / daysSinceCreation) / 10.0;
        }

        return HabitStatsRs.builder()
                .completionRate(completionRate)
                .currentStreak(currentStreak)
                .bestStreak(bestStreak)
                .totalCompletions(completedDays)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public DailyStatsRs getHabitDailyStats() {
        LocalDate today = LocalDate.now();

        List<Record> todayRecords = recordRepository.findByDate(today);

        Set<Long> completedHabitIds = todayRecords.stream()
                .map(record -> record.getHabit().getId())
                .collect(Collectors.toSet());

        List<Habit> allHabits = habitRepository.findAll();

        List<HabitDailyStatusRs> dailyStatusList = allHabits.stream()
                .map(habit -> HabitDailyStatusRs.builder()
                        .id(habit.getId())
                        .name(habit.getName())
                        .completed(completedHabitIds.contains(habit.getId()))
                        .build())
                .toList();

        return DailyStatsRs.builder()
                .date(today)
                .habits(dailyStatusList)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public WeeklyStatsRs getWeeklyHabitStats() {
        LocalDate today = LocalDate.now();
        LocalDate lastDay = today.minusDays(6);

        List<Record> weeklyRecords = recordRepository.findByDateBetween(lastDay, today);

        long habitCount = habitRepository.count();

        Map<LocalDate, Long> groupedWeeklyRecords = weeklyRecords.stream()
                .collect(Collectors.groupingBy(Record::getDate, Collectors.counting()));

        List<WeeklyProgressRs> weeklyProgress = new ArrayList<>();

        for (LocalDate date = lastDay; !date.isAfter(today); date = date.plusDays(1)) {
            Long completedAmount = groupedWeeklyRecords.getOrDefault(date, 0L);

            weeklyProgress.add(WeeklyProgressRs.builder()
                    .totalCount(habitCount)
                    .completedCount(completedAmount)
                    .date(date)
                    .build());
        }
        return new WeeklyStatsRs(weeklyProgress);
    }
}