package com.vladislav.controller;

import com.vladislav.dto.request.LoginRq;
import com.vladislav.dto.request.RegisterRq;
import com.vladislav.dto.response.LoginRs;
import com.vladislav.dto.response.RegisterRs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Authentication", description = "User registration and login. These endpoints are public.")
@SecurityRequirements
@RequestMapping("/api/auth")
public interface UserController {

    @Operation(
            summary = "Register a new user",
            description = "Creates a user account. Username must be unique (3-50 characters), "
                    + "password must be at least 6 characters, email is optional but must be valid if provided. "
                    + "Registration does not return a token - call /api/auth/login afterwards.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed (e.g. short password, invalid email)",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Username is already taken",
                    content = @Content(mediaType = "text/plain"))
    })
    @PostMapping("/register")
    ResponseEntity<RegisterRs> register(@RequestBody @Valid RegisterRq registerRq);

    @Operation(
            summary = "Log in and get a JWT token",
            description = "Checks the credentials and returns a JWT. "
                    + "Pass it in the header `Authorization: Bearer <token>` to call protected endpoints.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful, JWT token returned"),
            @ApiResponse(responseCode = "400", description = "Username or password is missing",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid username or password",
                    content = @Content(mediaType = "text/plain"))
    })
    @PostMapping("/login")
    ResponseEntity<LoginRs> login(@RequestBody @Valid LoginRq loginRq);
}
