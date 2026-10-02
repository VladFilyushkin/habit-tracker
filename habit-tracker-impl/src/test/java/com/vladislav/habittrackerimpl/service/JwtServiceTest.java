package com.vladislav.habittrackerimpl.service;

import com.vladislav.service.impl.JwtServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET = "test-secret-key-must-be-at-least-256-bits-long-for-hmac-sha256";
    private static final long EXPIRATION_MS = 3600000L;

    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        jwtService = buildJwtService(SECRET, EXPIRATION_MS);
    }

    private JwtServiceImpl buildJwtService(String secret, long expirationMs) {
        var service = new JwtServiceImpl();
        ReflectionTestUtils.setField(service, "secretKey", secret);
        ReflectionTestUtils.setField(service, "expirationMs", expirationMs);
        return service;
    }

    private UserDetails userDetails(String username) {
        return User.withUsername(username)
                .password("irrelevant")
                .authorities(Collections.emptyList())
                .build();
    }

    @Test
    void shouldGenerateTokenAndExtractUsername() {
        String token = jwtService.generateToken("vlad213");

        assertEquals("vlad213", jwtService.extractUsername(token));
    }

    @Test
    void shouldValidateFreshTokenForCorrectUser() {
        String token = jwtService.generateToken("vlad213");

        assertTrue(jwtService.isTokenValid(token, userDetails("vlad213")));
    }

    @Test
    void shouldRejectTokenForDifferentUsername() {
        String token = jwtService.generateToken("vlad213");

        assertFalse(jwtService.isTokenValid(token, userDetails("someone-else")));
    }

    @Test
    void shouldThrowWhenTokenIsExpired() {
        var expiredTokenIssuer = buildJwtService(SECRET, -1000L);
        String expiredToken = expiredTokenIssuer.generateToken("vlad213");

        assertThrows(ExpiredJwtException.class, () -> jwtService.extractUsername(expiredToken));
    }

    @Test
    void shouldThrowWhenTokenSignedWithDifferentSecret() {
        var otherJwtService = buildJwtService(
                "completely-different-secret-key-also-at-least-256-bits", EXPIRATION_MS);
        String foreignToken = otherJwtService.generateToken("vlad213");

        assertThrows(SignatureException.class, () -> jwtService.extractUsername(foreignToken));
    }
}