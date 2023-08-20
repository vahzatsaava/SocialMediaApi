package com.example.socialmediaapi.mapper;

import com.example.socialmediaapi.dto.auth.SignUpDto;
import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        FriendShipMapper.class
})
public interface UserMapper {
    UserDto toDto(User user);

    @InheritInverseConfiguration
    User toEntity(UserDto userDto);

    default User signUpToUser(SignUpDto signUpDto) {
        return User.builder().username(signUpDto.getFirstName())
                .email(signUpDto.getEmail())
                .password(signUpDto.getPassword())
                .build();
    }
}
