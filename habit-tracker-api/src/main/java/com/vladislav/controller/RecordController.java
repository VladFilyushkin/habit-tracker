package com.vladislav.controller;

import com.vladislav.dto.response.RecordRs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/habits/{habitId}/records")
public interface RecordController {

    @PostMapping
    ResponseEntity<RecordRs> markComplete(@PathVariable Long habitId);
    @GetMapping
    ResponseEntity<List<RecordRs>> getAllById(@PathVariable Long habitId);
}
