package com.vladislav.service.impl;

import com.vladislav.dto.request.LoginRq;
import com.vladislav.dto.request.RegisterRq;
import com.vladislav.dto.response.LoginRs;
import com.vladislav.dto.response.RegisterRs;
import com.vladislav.entity.User;
import com.vladislav.exception.InvalidCredentialException;
import com.vladislav.exception.UserNameAlreadyExistsException;
import com.vladislav.mapper.UserMapper;
import com.vladislav.repository.UserRepository;
import com.vladislav.service.JwtService;
import com.vladislav.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.vladislav.constant.MessageConstant.INVALID_CREDENTIAL;
import static com.vladislav.constant.MessageConstant.USERNAME_ALREADY_EXISTS_EXCEPTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Override
    public RegisterRs register(RegisterRq registerRq) {
        if (userRepository.existsByUsername(registerRq.getUsername())) {
            log.error(String.format(USERNAME_ALREADY_EXISTS_EXCEPTION, registerRq.getUsername()));
            throw new UserNameAlreadyExistsException(String.format(USERNAME_ALREADY_EXISTS_EXCEPTION, registerRq.getUsername()));
        }

        User savedUser = userRepository.save(userMapper.fromDtoToEntity(registerRq, passwordEncoder));
        log.info("User successfully registered with id={} and username={}", savedUser.getId(), savedUser.getUsername());
        return userMapper.fromEntityToDto(savedUser);
    }

    @Override
    public LoginRs login(LoginRq loginRq) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRq.getUsername(), loginRq.getPassword()));
        } catch (BadCredentialsException e) {
            log.error("Failed login attempt with username={}", loginRq.getUsername());
            throw new InvalidCredentialException(INVALID_CREDENTIAL);
        }
        log.info("User successfully logged with username={}", loginRq.getUsername());
        return new LoginRs(jwtService.generateToken(loginRq.getUsername()));
    }
}
