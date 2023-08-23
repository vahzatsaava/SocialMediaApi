package com.example.socialmediaapi.dto;

import com.example.socialmediaapi.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;

    @NotBlank(message = "Username must not be blank")
    @Size(min = 8, message = "Username must be at least 8 characters long")
    private String username;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    @Size(min = 8, message = "Email must be at least 8 characters long")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one letter, one digit, and one special character")
    private String password;

    private List<Role> roles;

    private List<PostDto> posts;
}
