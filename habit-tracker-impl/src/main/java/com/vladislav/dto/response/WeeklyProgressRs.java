package com.vladislav.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyProgressRs {

    private LocalDate date;
    private Long completedCount;
    private Long totalCount;
}
