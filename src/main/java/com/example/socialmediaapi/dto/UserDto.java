package com.example.socialmediaapi.dto;

import com.example.socialmediaapi.model.Role;
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
    private String username;
    private String email;
    private String token;
    private String password;
    private List<Role> roles;
    private List<Long> sentFriendRequestIds;
    private List<Long> receivedFriendRequestIds;
    private List<Long> friendIds;
    private List<Long> subscriptionIds;
}
