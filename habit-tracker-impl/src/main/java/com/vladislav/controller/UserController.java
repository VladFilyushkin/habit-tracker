package com.vladislav.controller;

import com.vladislav.dto.request.LoginRq;
import com.vladislav.dto.request.RegisterRq;
import com.vladislav.dto.response.LoginRs;
import com.vladislav.dto.response.RegisterRs;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/auth")
public interface UserController {
    @PostMapping("/register")
    ResponseEntity<RegisterRs> register(@RequestBody @Valid RegisterRq registerRq);

    @PostMapping("/login")
    ResponseEntity<LoginRs> login(@RequestBody @Valid LoginRq loginRq);
}
