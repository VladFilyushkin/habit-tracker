package com.vladislav.controller;

import com.vladislav.dto.response.DailyStatsRs;
import com.vladislav.dto.response.HabitStatsRs;
import com.vladislav.dto.response.WeeklyStatsRs;
import com.vladislav.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
public interface StatsController {

    @GetMapping("/habits/{id}/stats")
    ResponseEntity<HabitStatsRs> getHabitStats(@PathVariable Long id, @AuthenticationPrincipal User user);

    @GetMapping("/stats/daily")
    ResponseEntity<DailyStatsRs> getDailyStats(@AuthenticationPrincipal User user);

    @GetMapping("/stats/week")
    ResponseEntity<WeeklyStatsRs> getWeeklyStats(@AuthenticationPrincipal User user);
}
