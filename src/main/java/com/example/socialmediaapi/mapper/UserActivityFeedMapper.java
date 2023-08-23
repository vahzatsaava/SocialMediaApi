package com.example.socialmediaapi.mapper;

import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.dto.content.UserActivityFeedDto;
import com.example.socialmediaapi.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserActivityFeedMapper {

    default UserActivityFeedDto toDto(User user) {
        return UserActivityFeedDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }

    default UserActivityFeedDto toDto(UserDto userDto) {
        return UserActivityFeedDto.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .roles(userDto.getRoles())
                .build();
    }
}
