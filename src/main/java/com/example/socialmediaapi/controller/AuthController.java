package com.example.socialmediaapi.controller;

import com.example.socialmediaapi.dto.auth.CredentialsDto;
import com.example.socialmediaapi.dto.auth.JwtResponse;
import com.example.socialmediaapi.dto.auth.SignUpDto;
import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.exceptions.AppError;
import com.example.socialmediaapi.security.JwtTokenProvider;
import com.example.socialmediaapi.security.UserDetailsServiceImpl;
import com.example.socialmediaapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager manager;
    private final UserDetailsServiceImpl userDetails;


    @PostMapping("/login")
    @Operation(summary = "[US 1.1] Prove our users login ",
            description = "This API is used to login and authenticate.")
    public ResponseEntity<?> login(@RequestBody CredentialsDto credentialsDto) {
        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(credentialsDto.getEmail(), credentialsDto.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "password or login is not correct"), HttpStatus.UNAUTHORIZED);
        }

        UserDetails loadUserByUsername = userDetails.loadUserByUsername(credentialsDto.getEmail());
        String token = tokenProvider.generateToken(loadUserByUsername);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    @Operation(summary = "[US 1.2] Register new user for api ",
            description = "This API is used for registration .")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto) {
        UserDto userDto = userService.register(signUpDto);
        return ResponseEntity.created(URI.create("/users/" + userDto.getId()))
                .body(userDto);
    }
}
