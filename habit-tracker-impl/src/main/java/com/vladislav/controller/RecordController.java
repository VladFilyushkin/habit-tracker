package com.vladislav.controller;

import com.vladislav.dto.response.RecordRs;
import com.vladislav.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/habits/{habitId}/records")
public interface RecordController {

    @PostMapping
    ResponseEntity<RecordRs> markComplete(@PathVariable Long habitId,@AuthenticationPrincipal User user);
    @GetMapping
    ResponseEntity<List<RecordRs>> getAllById(@PathVariable Long habitId, @AuthenticationPrincipal User user);
}
