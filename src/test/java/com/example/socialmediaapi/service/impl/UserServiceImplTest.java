package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.dto.auth.SignUpDto;
import com.example.socialmediaapi.mapper.UserMapper;
import com.example.socialmediaapi.model.Role;
import com.example.socialmediaapi.model.User;
import com.example.socialmediaapi.repository.RoleRepository;
import com.example.socialmediaapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findByEmail() {
        String userEmail = "user@example.com";
        User user = new User();
        when(userRepository.findUserByEmail(userEmail)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(new UserDto());

        UserDto result = userService.findByEmail(userEmail);

        assertNotNull(result);
        verify(userRepository, times(1)).findUserByEmail(userEmail);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void register() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setEmail("user@example.com");
        signUpDto.setPassword("password");

        User newUser = new User();
        when(userRepository.findUserByEmail(signUpDto.getEmail())).thenReturn(Optional.empty());
        when(userMapper.signUpToUser(signUpDto)).thenReturn(newUser);
        when(roleRepository.findAllByName("ROLE_USER")).thenReturn(Optional.of(new Role()));
        when(passwordEncoder.encode(signUpDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(newUser)).thenReturn(newUser);
        when(userMapper.toDto(newUser)).thenReturn(new UserDto());

        UserDto result = userService.register(signUpDto);

        assertNotNull(result);
        verify(userRepository, times(1)).findUserByEmail(signUpDto.getEmail());
        verify(userMapper, times(1)).signUpToUser(signUpDto);
        verify(roleRepository, times(1)).findAllByName("ROLE_USER");
        verify(passwordEncoder, times(1)).encode(signUpDto.getPassword());
        verify(userRepository, times(1)).save(newUser);
        verify(userMapper, times(1)).toDto(newUser);
    }

    @Test
    void getUserByEmail() {
        String userEmail = "user@example.com";
        User user = new User();
        when(userRepository.findUserByEmail(userEmail)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(new UserDto());

        UserDto result = userService.getUserByEmail(userEmail);

        assertNotNull(result);
        verify(userRepository, times(1)).findUserByEmail(userEmail);
        verify(userMapper, times(1)).toDto(user);
    }
}