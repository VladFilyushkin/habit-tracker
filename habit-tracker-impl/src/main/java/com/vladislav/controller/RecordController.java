package com.vladislav.controller;

import com.vladislav.dto.response.RecordRs;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Records", description = "Daily completion marks of a habit. Requires a JWT token.")
@RequestMapping("/api/habits/{habitId}/records")
public interface RecordController {

    @Operation(
            summary = "Mark a habit as completed today",
            description = "Creates a record for today's date (server time). "
                    + "A habit can be marked only once per day.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Habit marked as completed for today"),
            @ApiResponse(responseCode = "401", description = "Missing, invalid or expired token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Habit not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "Habit is already marked today",
                    content = @Content(mediaType = "text/plain"))
    })
    @PostMapping
    ResponseEntity<RecordRs> markComplete(@PathVariable Long habitId, @AuthenticationPrincipal User user);

    @Operation(
            summary = "Get all records of a habit",
            description = "Returns every completion record of the habit. The list is empty if the habit was never marked.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of records"),
            @ApiResponse(responseCode = "401", description = "Missing, invalid or expired token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Habit not found", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping
    ResponseEntity<List<RecordRs>> getAllById(@PathVariable Long habitId, @AuthenticationPrincipal User user);
}
