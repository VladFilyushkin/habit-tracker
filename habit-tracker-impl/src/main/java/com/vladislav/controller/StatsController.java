package com.vladislav.controller;

import com.vladislav.dto.response.DailyStatsRs;
import com.vladislav.dto.response.HabitStatsRs;
import com.vladislav.dto.response.WeeklyStatsRs;
import com.vladislav.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
@Tag(name = "Statistics", description = "Streaks and progress statistics. Requires a JWT token.")
@RequestMapping("/api")
public interface StatsController {

    @Operation(
            summary = "Get statistics of a habit",
            description = "Returns the current streak (consecutive days ending today), the best streak, "
                    + "the completion rate in percent since the habit was created and the total number of completions.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habit statistics"),
            @ApiResponse(responseCode = "401", description = "Missing, invalid or expired token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Habit not found", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/habits/{id}/stats")
    ResponseEntity<HabitStatsRs> getHabitStats(@PathVariable Long id, @AuthenticationPrincipal User user);


    @Operation(
            summary = "Get today's progress",
            description = "Lists all habits of the authenticated user with a flag showing whether each one is completed today.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Today's status of every habit"),
            @ApiResponse(responseCode = "401", description = "Missing, invalid or expired token", content = @Content)
    })
    @GetMapping("/stats/daily")
    ResponseEntity<DailyStatsRs> getDailyStats(@AuthenticationPrincipal User user);

    @Operation(
            summary = "Get progress for the last 7 days",
            description = "Returns 7 entries (today and the 6 previous days), each with the number of completed habits "
                    + "and the total number of the user's habits.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Weekly progress"),
            @ApiResponse(responseCode = "401", description = "Missing, invalid or expired token", content = @Content)
    })
    @GetMapping("/stats/week")
    ResponseEntity<WeeklyStatsRs> getWeeklyStats(@AuthenticationPrincipal User user);
}
