package com.vladislav.controller.impl;

import com.vladislav.controller.StatsController;
import com.vladislav.dto.response.DailyStatsRs;
import com.vladislav.dto.response.HabitStatsRs;
import com.vladislav.dto.response.WeeklyStatsRs;
import com.vladislav.entity.User;
import com.vladislav.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatsControllerImpl implements StatsController {

    private final StatsService statsService;

    @Override
    public ResponseEntity<HabitStatsRs> getHabitStats(Long id, User user) {
        return ResponseEntity.ok().body(statsService.getHabitStats(id, user));
    }

    @Override
    public ResponseEntity<DailyStatsRs> getDailyStats(User user) {
        return ResponseEntity.ok().body(statsService.getHabitDailyStats(user));
    }

    @Override
    public ResponseEntity<WeeklyStatsRs> getWeeklyStats(User user) {
        return ResponseEntity.ok().body(statsService.getWeeklyHabitStats(user));
    }
}
