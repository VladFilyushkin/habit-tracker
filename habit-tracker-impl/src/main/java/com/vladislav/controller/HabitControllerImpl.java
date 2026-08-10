package com.vladislav.controller;

import com.vladislav.dto.request.HabitRq;
import com.vladislav.dto.request.UpdatedHabitRq;
import com.vladislav.dto.response.HabitRs;
import com.vladislav.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HabitControllerImpl implements HabitController {

    private final HabitService habitService;

    @Override
    public ResponseEntity<HabitRs> save(HabitRq habitRq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(habitService.save(habitRq));
    }

    @Override
    public ResponseEntity<HabitRs> getById(Long id) {
        return ResponseEntity.ok().body(habitService.findById(id));
    }

    @Override
    public ResponseEntity<List<HabitRs>> getAll() {
        return ResponseEntity.ok().body(habitService.findAll());
    }

    @Override
    public ResponseEntity<HabitRs> update(Long id, UpdatedHabitRq updatedHabitRq) {
        return ResponseEntity.ok().body(habitService.update(id, updatedHabitRq));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        habitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
