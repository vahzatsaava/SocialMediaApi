package com.example.socialmediaapi.controller;

import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.dto.general.ResponseDto;
import com.example.socialmediaapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final UserService userService;

    @GetMapping("/get")
    public String getName() {
        return "Hello my Dear Friend !!!!!!!!!!!!!!!!!!!!!!!!!!!!!";
    }

    @GetMapping("/get-by-email")
    @Operation(summary = "[US 3.] Get user by email ",
            description = "This API is used to get User info by email.")
    public ResponseEntity<ResponseDto<UserDto>> getUserByEmail(@RequestParam String email) {
        UserDto userDto = userService.getUserByEmail(email);
        ResponseDto<UserDto> responseDto = new ResponseDto<>(HttpStatus.OK.value(), userDto);

        return ResponseEntity.ok(responseDto);
    }
}
