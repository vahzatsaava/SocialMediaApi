package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.SubscriptionDto;
import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.mapper.SubscriptionMapper;
import com.example.socialmediaapi.mapper.UserMapper;
import com.example.socialmediaapi.model.Subscription;
import com.example.socialmediaapi.model.User;
import com.example.socialmediaapi.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceImplTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private SubscriptionMapper subscriptionMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    @Test
    void save() {
        User followerUser = new User();
        User targetUser = new User();

        Subscription subscription = new Subscription();
        subscription.setFollowerUser(followerUser);
        subscription.setTargetUser(targetUser);

        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);
        when(subscriptionMapper.toDto(subscription)).thenReturn(new SubscriptionDto());

        SubscriptionDto result = subscriptionService.save(followerUser, targetUser);

        assertNotNull(result);
        verify(subscriptionRepository, times(1)).save(subscription);
        verify(subscriptionMapper, times(1)).toDto(subscription);
    }

    @Test
    void delete() {
        Long subscriptionId = 1L;
        Subscription subscription = new Subscription();

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));
        when(subscriptionMapper.toDto(subscription)).thenReturn(new SubscriptionDto());

        SubscriptionDto result = subscriptionService.delete(subscriptionId);

        assertNotNull(result);
        verify(subscriptionRepository, times(1)).delete(subscription);
        verify(subscriptionMapper, times(1)).toDto(subscription);
    }

    @Test
    void findByTargetUserEmail() {
        String targetUserEmail = "target@example.com";
        List<Subscription> subscriptions = new ArrayList<>();
        User user1 = new User();
        User user2 = new User();

        subscriptions.add(new Subscription(1L,user1, user2));
        subscriptions.add(new Subscription(1L,user2, user1));

        when(subscriptionRepository.findByTargetUserEmail(targetUserEmail)).thenReturn(Optional.of(subscriptions));
        when(userMapper.toDto(user1)).thenReturn(new UserDto());
        when(userMapper.toDto(user2)).thenReturn(new UserDto());

        List<UserDto> result = subscriptionService.findByTargetUserEmail(targetUserEmail);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(subscriptionRepository, times(1)).findByTargetUserEmail(targetUserEmail);
        verify(userMapper, times(2)).toDto(any(User.class));
    }

    @Test
    void deleteByFollowerAndTargetEmails() {
        String senderUserEmail = "sender@example.com";
        String userToDeleteEmail = "userToDelete@example.com";

        subscriptionService.deleteByFollowerAndTargetEmails(senderUserEmail, userToDeleteEmail);

        verify(subscriptionRepository, times(1)).deleteByFollowerUserEmailOrTargetUserEmail(senderUserEmail, userToDeleteEmail);
    }
}