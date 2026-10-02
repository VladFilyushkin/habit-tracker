package com.vladislav.controller;

import com.vladislav.dto.request.HabitRq;
import com.vladislav.dto.request.UpdatedHabitRq;
import com.vladislav.dto.response.HabitRs;
import com.vladislav.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Habits", description = "CRUD operations for the current user's habits. Requires a JWT token.")
@RequestMapping("api/habits")
public interface HabitController {

    @Operation(
            summary = "Create a habit",
            description = "Creates a new habit that belongs to the authenticated user. "
                    + "All fields are required, target must be a positive number.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Habit created"),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing, invalid or expired token", content = @Content)
    })
    @PostMapping()
    ResponseEntity<HabitRs> save(@RequestBody @Valid HabitRq habitRq, @AuthenticationPrincipal User user);

    @Operation(
            summary = "Get a habit by id",
            description = "Returns a habit only if it belongs to the authenticated user. "
                    + "Habits of other users are reported as not found.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habit found"),
            @ApiResponse(responseCode = "401", description = "Missing, invalid or expired token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Habit not found", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/{id}")
    ResponseEntity<HabitRs> getById(@PathVariable Long id, @AuthenticationPrincipal User user);

    @Operation(
            summary = "Get all habits",
            description = "Returns all habits of the authenticated user. The list is empty if there are none.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of habits"),
            @ApiResponse(responseCode = "401", description = "Missing, invalid or expired token", content = @Content)
    })
    @GetMapping()
    ResponseEntity<List<HabitRs>> getAll(@AuthenticationPrincipal User user);

    @Operation(
            summary = "Update a habit",
            description = "Replaces name, description and target of an existing habit of the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habit updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing, invalid or expired token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Habit not found", content = @Content(mediaType = "text/plain"))
    })
    @PutMapping("/{id}")
    ResponseEntity<HabitRs> update(@PathVariable Long id, @Valid @RequestBody UpdatedHabitRq updatedHabitRq, @AuthenticationPrincipal User user);

    @Operation(
            summary = "Delete a habit",
            description = "Deletes the habit together with all of its records.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Habit deleted"),
            @ApiResponse(responseCode = "401", description = "Missing, invalid or expired token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Habit not found", content = @Content(mediaType = "text/plain"))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user);
}
