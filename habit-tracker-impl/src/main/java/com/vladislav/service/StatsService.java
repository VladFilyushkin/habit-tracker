package com.vladislav.service;

import com.vladislav.dto.response.DailyStatsRs;
import com.vladislav.dto.response.HabitStatsRs;
import com.vladislav.dto.response.WeeklyStatsRs;
import com.vladislav.entity.User;

public interface StatsService {

    HabitStatsRs getHabitStats(Long habitId, User user);

    DailyStatsRs getHabitDailyStats(User user);

    WeeklyStatsRs getWeeklyHabitStats(User user);
}
