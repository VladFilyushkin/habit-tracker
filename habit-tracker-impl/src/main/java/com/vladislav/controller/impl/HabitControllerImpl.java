package com.vladislav.controller.impl;

import com.vladislav.controller.HabitController;
import com.vladislav.dto.request.HabitRq;
import com.vladislav.dto.request.UpdatedHabitRq;
import com.vladislav.dto.response.HabitRs;
import com.vladislav.entity.User;
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
    public ResponseEntity<HabitRs> save(HabitRq habitRq, User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(habitService.save(habitRq, user));
    }

    @Override
    public ResponseEntity<HabitRs> getById(Long id, User user) {
        return ResponseEntity.ok().body(habitService.findById(id, user));
    }

    @Override
    public ResponseEntity<List<HabitRs>> getAll(User user) {
        return ResponseEntity.ok().body(habitService.findAll(user));
    }

    @Override
    public ResponseEntity<HabitRs> update(Long id, UpdatedHabitRq updatedHabitRq, User user) {
        return ResponseEntity.ok().body(habitService.update(id, updatedHabitRq,user));
    }

    @Override
    public ResponseEntity<Void> delete(Long id, User user) {
        habitService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
