package com.vladislav.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedHabitRq {

    @NotBlank(message = "Name can't be null")
    private String name;
    @NotBlank(message = "Description can't be null")
    private String description;
    @NotNull(message = "Target can't be null")
    @Positive(message = "Target cant be negative")
    private Integer target;
}
