package com.example.socialmediaapi.service;

import com.example.socialmediaapi.dto.message.ContentChatFriendsDto;

import java.util.List;

public interface MessageService {
    void sendMessage(String senderEmail,String receiverEmail,String content);
    List<ContentChatFriendsDto> getChatBetweenUsers(String emailSender, String emailReceiver);
}
