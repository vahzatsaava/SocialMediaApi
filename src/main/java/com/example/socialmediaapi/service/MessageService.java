package com.example.socialmediaapi.service;

import com.example.socialmediaapi.dto.message.ContentChatFriendsDto;

import java.security.Principal;
import java.util.List;

public interface MessageService {
    void sendMessage(Principal principal, String receiverEmail, String content);
    List<ContentChatFriendsDto> getChatBetweenUsers(Principal principal, String emailReceiver);
}
