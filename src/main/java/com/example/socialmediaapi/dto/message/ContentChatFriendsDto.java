package com.example.socialmediaapi.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContentChatFriendsDto {
    private Long senderUser;
    private Long receiverUser;
    private String message;
    private LocalDateTime sentAt;
}
