package com.vladislav.habittrackerimpl.service;

import com.vladislav.exception.InvalidCredentialException;
import com.vladislav.exception.UserNameAlreadyExistsException;
import com.vladislav.mapper.UserMapper;
import com.vladislav.repository.UserRepository;
import com.vladislav.service.JwtService;
import com.vladislav.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.vladislav.habittrackerimpl.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void shouldSuccessfullyRegisterUser() {
        var registerRq = getRegisterRqForUnit();
        var registerRs = getRegisterRsForUnit();
        var user = getUserForUnit();

        when(userRepository.existsByUsername(registerRq.getUsername())).thenReturn(false);
        when(userMapper.fromDtoToEntity(registerRq, passwordEncoder)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.fromEntityToDto(user)).thenReturn(registerRs);

        var result = userService.register(registerRq);

        assertEquals(registerRq.getUsername(), result.getUsername());

        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        var registerRq = getRegisterRqForUnit();

        when(userRepository.existsByUsername(registerRq.getUsername())).thenReturn(true);

        assertThrows(UserNameAlreadyExistsException.class, () -> userService.register(registerRq));

        verify(userRepository, never()).save(any());
        verifyNoInteractions(userMapper);
    }

    @Test
    void shouldLoginSuccessfullyAndReturnToken() {
        var loginRq = getLoginRqForUnit();
        var loginRs = getLoginRsForUnit();

        when(jwtService.generateToken(loginRq.getUsername())).thenReturn(loginRs.getToken());

        var result = userService.login(loginRq);

        assertEquals(loginRs.getToken(), result.getToken());
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(loginRq.getUsername(), loginRq.getPassword()));

    }

    @Test
    void shouldThrowInvalidCredentialExceptionWhenPasswordIsWrong() {
        var loginRq = getLoginRqForUnit();
        loginRq.setPassword("wrongPasswrd");
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any());

        assertThrows(InvalidCredentialException.class, () -> userService.login(loginRq));

        verifyNoInteractions(jwtService);
    }
}
