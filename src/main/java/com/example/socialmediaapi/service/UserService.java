package com.example.socialmediaapi.service;

import com.example.socialmediaapi.dto.auth.CredentialsDto;
import com.example.socialmediaapi.dto.auth.SignUpDto;
import com.example.socialmediaapi.dto.UserDto;

public interface UserService {

    UserDto findByEmail(String email);
    UserDto login(CredentialsDto credentialsDto);
    UserDto register(SignUpDto signUpDto);
    UserDto findUserByUserName(String userName);
}
