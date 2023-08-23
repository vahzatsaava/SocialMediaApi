package com.example.socialmediaapi.dto.content;

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
public class UserActivityFeedDto {
    private Long id;
    private String username;
    private String email;
    private List<Role> roles;

}
