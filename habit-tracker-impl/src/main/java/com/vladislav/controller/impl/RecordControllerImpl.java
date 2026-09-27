package com.vladislav.controller.impl;

import com.vladislav.controller.RecordController;
import com.vladislav.dto.response.RecordRs;
import com.vladislav.entity.User;
import com.vladislav.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecordControllerImpl implements RecordController {

    private final RecordService recordService;

    @Override
    public ResponseEntity<RecordRs> markComplete(Long habitId, User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recordService.markComplete(habitId, user));
    }

    @Override
    public ResponseEntity<List<RecordRs>> getAllById(Long habitId, User user) {
        return ResponseEntity.ok().body(recordService.getAllByHabitId(habitId, user));
    }
}
