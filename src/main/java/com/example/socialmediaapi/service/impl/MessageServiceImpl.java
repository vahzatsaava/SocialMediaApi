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

import java.security.Principal;
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
    public void sendMessage(Principal principalSenderEmail, String receiverEmail, String content) {

        if (receiverEmail == null) {
            throw new IllegalArgumentException ("receiverEmail is null");
        }
        if (content == null) {
            throw new IllegalArgumentException ("content is null");
        }
        if (Boolean.FALSE.equals(friendShipService.isFriends(principalSenderEmail.getName(), receiverEmail))) {
            throw new MessageSendException("Users with emails " + principalSenderEmail + " and " + receiverEmail + " are not friends");
        }

        UserDto senderUser = userService.findByEmail(principalSenderEmail.getName());
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
    public List<ContentChatFriendsDto> getChatBetweenUsers(Principal principal, String emailReceiver) {

        if (emailReceiver == null) {
            throw new IllegalArgumentException ("emailReceiver is null");
        }
        if (Boolean.FALSE.equals(friendShipService.isFriends(principal.getName(), emailReceiver))) {
            throw new IllegalArgumentException ("Users with emails " + principal.getName() + " and " + emailReceiver + " are not friends");
        }

        UserDto userDtoSender = userService.findByEmail(principal.getName());
        UserDto userDtoReceiver = userService.findByEmail(emailReceiver);

        User userSender = userMapper.toEntity(userDtoSender);
        User userReceiver = userMapper.toEntity(userDtoReceiver);

        List<Message> messageList = messageRepository.findChatMessages(userSender, userReceiver);

        return messageList.stream()
                .map(contentChatFriendsDtoMapper::toContentChat)
                .toList();
    }
}
