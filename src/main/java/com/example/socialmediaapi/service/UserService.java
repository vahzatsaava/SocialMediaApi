package com.example.socialmediaapi.service;

import com.example.socialmediaapi.dto.auth.SignUpDto;
import com.example.socialmediaapi.dto.UserDto;

public interface UserService {

    UserDto findByEmail(String email);
    UserDto register(SignUpDto signUpDto);
    UserDto getUserByEmail(String userEmail);

}
