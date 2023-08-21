package com.example.socialmediaapi.mapper.message;

import com.example.socialmediaapi.dto.message.ContentChatFriendsDto;
import com.example.socialmediaapi.model.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContentChatFriendsDtoMapper {

    default ContentChatFriendsDto toContentChat(Message message) {
        return ContentChatFriendsDto.builder()
                .senderUser(message.getSenderUser().getId())
                .receiverUser(message.getReceiverUser().getId())
                .sentAt(message.getSentAt())
                .message(message.getContent())
                .build();
    }
}
