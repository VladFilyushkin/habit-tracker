package com.vladislav.controller;

import com.vladislav.dto.request.HabitRq;
import com.vladislav.dto.request.UpdatedHabitRq;
import com.vladislav.dto.response.HabitRs;
import com.vladislav.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/habits")
public interface HabitController {

    @PostMapping()
    ResponseEntity<HabitRs> save(@RequestBody @Valid HabitRq habitRq, @AuthenticationPrincipal User user);

    @GetMapping("/{id}")
    ResponseEntity<HabitRs> getById(@PathVariable Long id, @AuthenticationPrincipal User user);

    @GetMapping()
    ResponseEntity<List<HabitRs>> getAll(@AuthenticationPrincipal User user);

    @PutMapping("/{id}")
    ResponseEntity<HabitRs> update(@PathVariable Long id, @Valid @RequestBody UpdatedHabitRq updatedHabitRq, @AuthenticationPrincipal User user);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user);
}
