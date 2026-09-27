package com.vladislav.controller.impl;

import com.vladislav.controller.UserController;
import com.vladislav.dto.request.LoginRq;
import com.vladislav.dto.request.RegisterRq;
import com.vladislav.dto.response.LoginRs;
import com.vladislav.dto.response.RegisterRs;
import com.vladislav.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<RegisterRs> register(RegisterRq registerRq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(registerRq));
    }

    @Override
    public ResponseEntity<LoginRs> login(LoginRq loginRq) {
        return ResponseEntity.ok().body(userService.login(loginRq));
    }
}
