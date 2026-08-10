package com.vladislav.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordRs {
    private Long id;
    private Long habitId;
    private LocalDate date;
}
