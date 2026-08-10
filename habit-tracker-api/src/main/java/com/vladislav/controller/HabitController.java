package com.vladislav.controller;

import com.vladislav.dto.request.HabitRq;
import com.vladislav.dto.request.UpdatedHabitRq;
import com.vladislav.dto.response.HabitRs;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/habits")
public interface HabitController {

    @PostMapping()
    ResponseEntity<HabitRs> save(@RequestBody @Valid HabitRq habitRq);

    @GetMapping("/{id}")
    ResponseEntity<HabitRs> getById(@PathVariable Long id);

    @GetMapping()
    ResponseEntity<List<HabitRs>> getAll();

    @PutMapping("/{id}")
    ResponseEntity<HabitRs> update(@PathVariable Long id, @Valid @RequestBody UpdatedHabitRq updatedHabitRq);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}
