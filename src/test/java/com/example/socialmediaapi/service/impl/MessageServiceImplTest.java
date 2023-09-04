package com.example.socialmediaapi.service.impl;


import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.dto.message.ContentChatFriendsDto;
import com.example.socialmediaapi.dto.message.MessageDto;
import com.example.socialmediaapi.mapper.UserMapper;
import com.example.socialmediaapi.mapper.message.ContentChatFriendsDtoMapper;
import com.example.socialmediaapi.mapper.message.MessageMapper;
import com.example.socialmediaapi.model.Message;
import com.example.socialmediaapi.model.User;
import com.example.socialmediaapi.repository.MessageRepository;
import com.example.socialmediaapi.service.FriendShipService;
import com.example.socialmediaapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {
    @Mock
    private FriendShipService friendShipService;

    @Mock
    private UserService userService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ContentChatFriendsDtoMapper contentChatFriendsDtoMapper;

    @InjectMocks
    private MessageServiceImpl messageService;
/*
    @Test
    void sendMessage() {
        UserDto senderUser = new UserDto();
        UserDto receiverUser = new UserDto();
        MessageDto messageDto = new MessageDto();

        lenient().when(userService.findByEmail(anyString())).thenReturn(senderUser).thenReturn(receiverUser);
        lenient().when(userMapper.toEntity(any(UserDto.class))).thenReturn(new User());
        lenient().when(messageMapper.toDto(any(Message.class))).thenReturn(messageDto);
        lenient().when(friendShipService.isFriends(anyString(), anyString())).thenReturn(true);


        messageService.sendMessage("sender@example.com", "receiver@example.com", "Hello");

        verify(friendShipService).isFriends(eq("sender@example.com"), eq("receiver@example.com"));
        verify(userService, times(2)).findByEmail(anyString());
        verify(userMapper, times(2)).toEntity(any(UserDto.class));
        verify(messageRepository).save(any(Message.class));
    }

 */
/*
    @Test
    void getChatBetweenUsers() {
        UserDto senderUserDto = new UserDto();
        UserDto receiverUserDto = new UserDto();
        User userSender = new User();
        User userReceiver = new User();
        Message message = new Message();
        List<Message> messageList = Collections.singletonList(message);
        ContentChatFriendsDto contentChatDto = new ContentChatFriendsDto();
        when(userService.findByEmail(anyString())).thenReturn(senderUserDto).thenReturn(receiverUserDto);
        when(userMapper.toEntity(any(UserDto.class))).thenReturn(userSender).thenReturn(userReceiver);
        when(messageRepository.findChatMessages(any(User.class), any(User.class))).thenReturn(messageList);
        when(contentChatFriendsDtoMapper.toContentChat(any(Message.class))).thenReturn(contentChatDto);
        lenient().when(friendShipService.isFriends(anyString(),anyString())).thenReturn(true);

        List<ContentChatFriendsDto> chatMessages = messageService.getChatBetweenUsers("sender@example.com", "receiver@example.com");

        verify(friendShipService).isFriends(eq("sender@example.com"), eq("receiver@example.com"));
        verify(userService, times(2)).findByEmail(anyString());
        verify(userMapper, times(2)).toEntity(any(UserDto.class));
        verify(messageRepository).findChatMessages(any(User.class), any(User.class));
        verify(contentChatFriendsDtoMapper).toContentChat(any(Message.class));
        assertEquals(1, chatMessages.size());
    }

 */
}