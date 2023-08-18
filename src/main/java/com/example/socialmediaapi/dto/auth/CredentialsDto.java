package com.example.socialmediaapi.dto.auth;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CredentialsDto {
    private String email;
    private String username;
    private String password;
}
