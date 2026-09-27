package com.vladislav.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyStatsRs {

  List<WeeklyProgressRs> week;
}
