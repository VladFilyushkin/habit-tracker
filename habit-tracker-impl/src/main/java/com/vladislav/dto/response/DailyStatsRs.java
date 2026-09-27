package com.vladislav.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyStatsRs {

    private LocalDate date;
    private List<HabitDailyStatusRs> habits;
}
