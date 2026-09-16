package com.vladislav.service;

import com.vladislav.dto.response.DailyStatsRs;
import com.vladislav.dto.response.HabitStatsRs;
import com.vladislav.dto.response.WeeklyStatsRs;

public interface StatsService {

    HabitStatsRs getHabitStats(Long habitId);

    DailyStatsRs getHabitDailyStats();

    WeeklyStatsRs getWeeklyHabitStats();
}
