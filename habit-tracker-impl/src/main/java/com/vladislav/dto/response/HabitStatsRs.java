package com.vladislav.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HabitStatsRs {

    private Integer currentStreak;
    private Integer bestStreak;
    private Double completionRate;
    private Integer totalCompletions;
}
