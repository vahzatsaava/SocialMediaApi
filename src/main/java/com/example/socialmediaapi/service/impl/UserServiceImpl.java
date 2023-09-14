package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.auth.SignUpDto;
import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.exceptions.EntityIsPresentException;
import com.example.socialmediaapi.mapper.UserMapper;
import com.example.socialmediaapi.model.User;
import com.example.socialmediaapi.repository.RoleRepository;
import com.example.socialmediaapi.repository.UserRepository;
import com.example.socialmediaapi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        if (email == null){
            throw new IllegalArgumentException ("email is null check email value ");
        }
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto register(SignUpDto signUpDto) {
        if (signUpDto == null){
            throw new IllegalArgumentException ("signUpDto is null check signUpDto value");
        }
        Optional<User> user = userRepository.findUserByEmail(signUpDto.getEmail());
        if (user.isPresent()){
            throw new EntityIsPresentException("user was created before");
        }
        log.info("Try to save new user ");
        User newUser = userMapper.signUpToUser(signUpDto);
        newUser.setRoles(List.of(roleRepository.findAllByName("ROLE_USER").get()));
        newUser.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        User savedUser = userRepository.save(newUser);

        return userMapper.toDto(savedUser);
    }




}
