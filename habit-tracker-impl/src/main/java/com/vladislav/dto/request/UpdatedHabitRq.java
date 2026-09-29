package com.vladislav.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatedHabitRq {

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;
    @NotBlank(message = "Description is required")
    @Size(max = 5000, message = "Description must be less than 5000 characters")
    private String description;
    @NotNull(message = "Target is required")
    @Positive(message = "Target cant be negative")
    @Max(value = 2147483647, message = "Target value exceeds integer limit")
    private Integer target;
}
