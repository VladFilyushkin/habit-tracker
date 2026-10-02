package com.vladislav.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HabitRs {

    private Long id;
    private String name;
    private String description;
    private Integer target;
    private LocalDateTime createdAt;
}
