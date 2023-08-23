package com.example.socialmediaapi.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpDto {
    @NotBlank(message = "First name must not be blank")
    @Size(min = 8, message = "First name must be at least 8 characters long")
    private String firstName;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    @Size(min = 8, message = "Email must be at least 8 characters long")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one letter, one digit, and one special character")
    private String password;

}
