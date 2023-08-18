package com.example.socialmediaapi.controller;

import com.example.socialmediaapi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/get")
    public String getName(){
        return "Hello my Dear Friend !!!!!!!!!!!!!!!!!!!!!!!!!!!!!";
    }
}
