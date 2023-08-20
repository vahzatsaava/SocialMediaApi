package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.auth.CredentialsDto;
import com.example.socialmediaapi.dto.auth.SignUpDto;
import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.exceptions.EntityIsPresentException;
import com.example.socialmediaapi.mapper.UserMapper;
import com.example.socialmediaapi.model.User;
import com.example.socialmediaapi.repository.RoleRepository;
import com.example.socialmediaapi.repository.UserRepository;
import com.example.socialmediaapi.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
            throw new NullPointerException("email is null check email value");
        }
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findUserByEmail(credentialsDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + credentialsDto.getEmail()));
            return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto register(SignUpDto signUpDto) {
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

    @Override
    @Transactional(readOnly = true)
    public UserDto findUserByUserName(String userName) {
        User user = userRepository.findUserByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userName));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String userEmail) {
        if (userEmail == null) {
            throw new NullPointerException("email is null check email value");
        }
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email - " + userEmail));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(Long userId) {
        if (userId == null) {
            throw new NullPointerException("userId is null check email value");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id - " + userId));

        return userMapper.toDto(user);
    }


}
