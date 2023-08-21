package com.example.socialmediaapi.dto.message;
import com.example.socialmediaapi.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {

    private UserDto senderUser;

    private UserDto receiverUser;

    private String content;

    private LocalDateTime sentAt;
}
