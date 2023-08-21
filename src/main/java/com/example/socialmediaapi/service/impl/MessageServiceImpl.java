package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.dto.message.ContentChatFriendsDto;
import com.example.socialmediaapi.exceptions.MessageSendException;
import com.example.socialmediaapi.mapper.message.ContentChatFriendsDtoMapper;
import com.example.socialmediaapi.mapper.message.MessageMapper;
import com.example.socialmediaapi.mapper.UserMapper;
import com.example.socialmediaapi.model.Message;
import com.example.socialmediaapi.model.User;
import com.example.socialmediaapi.repository.MessageRepository;
import com.example.socialmediaapi.service.FriendShipService;
import com.example.socialmediaapi.service.MessageService;
import com.example.socialmediaapi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final FriendShipService friendShipService;
    private final UserService userService;

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserMapper userMapper;
    private final ContentChatFriendsDtoMapper contentChatFriendsDtoMapper;

    @Override
    @Transactional
    public void sendMessage(String senderEmail, String receiverEmail, String content) {
        if (senderEmail == null) {
            throw new MessageSendException("sendEmail is null");
        }
        if (receiverEmail == null) {
            throw new MessageSendException("receiverEmail is null");
        }
        if (content == null) {
            throw new MessageSendException("content is null");
        }
        if (Boolean.FALSE.equals(friendShipService.isFriends(senderEmail, receiverEmail))) {
            throw new MessageSendException("Users with emails " + senderEmail + " and " + receiverEmail + " are not friends");
        }

        UserDto senderUser = userService.findByEmail(senderEmail);
        UserDto receiverUser = userService.findByEmail(receiverEmail);

        Message message = new Message();
        message.setContent(content);
        message.setReceiverUser(userMapper.toEntity(receiverUser));
        message.setSenderUser(userMapper.toEntity(senderUser));
        message.setSentAt(LocalDateTime.now());

        messageRepository.save(message);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContentChatFriendsDto> getChatBetweenUsers(String emailSender, String emailReceiver) {
        if (emailSender == null) {
            throw new MessageSendException("emailSender is null");
        }
        if (emailReceiver == null) {
            throw new MessageSendException("emailReceiver is null");
        }
        if (Boolean.FALSE.equals(friendShipService.isFriends(emailSender, emailReceiver))) {
            throw new MessageSendException("Users with emails " + emailSender + " and " + emailReceiver + " are not friends");
        }

        UserDto userDtoSender = userService.findByEmail(emailSender);
        UserDto userDtoReceiver = userService.findByEmail(emailReceiver);

        User userSender = userMapper.toEntity(userDtoSender);
        User userReceiver = userMapper.toEntity(userDtoReceiver);

        List<Message> messageList = messageRepository.findChatMessages(userSender, userReceiver);

        return messageList.stream()
                .map(contentChatFriendsDtoMapper::toContentChat)
                .toList();
    }
}
