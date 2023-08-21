package com.example.socialmediaapi.dto;

import com.example.socialmediaapi.model.enums.FriendRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendRequestDto {


    private UserDto senderUser;

    private UserDto receiverUser;

    private FriendRequestStatus status;

}
