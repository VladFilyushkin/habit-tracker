package com.vladislav.controller;

import com.vladislav.dto.response.DailyStatsRs;
import com.vladislav.dto.response.HabitStatsRs;
import com.vladislav.dto.response.WeeklyStatsRs;
import com.vladislav.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatsControllerImpl implements StatsController {

    private final StatsService statsService;

    @Override
    public ResponseEntity<HabitStatsRs> getHabitStats(Long id) {
        return ResponseEntity.ok().body(statsService.getHabitStats(id));
    }

    @Override
    public ResponseEntity<DailyStatsRs> getDailyStats() {
        return ResponseEntity.ok().body(statsService.getHabitDailyStats());
    }

    @Override
    public ResponseEntity<WeeklyStatsRs> getWeeklyStats() {
        return ResponseEntity.ok().body(statsService.getWeeklyHabitStats());
    }
}
