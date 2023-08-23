package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.SubscriptionDto;
import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.mapper.SubscriptionMapper;
import com.example.socialmediaapi.mapper.UserMapper;
import com.example.socialmediaapi.model.Subscription;
import com.example.socialmediaapi.model.User;
import com.example.socialmediaapi.repository.SubscriptionRepository;
import com.example.socialmediaapi.service.SubscriptionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final SubscriptionMapper subscriptionMapper;
    private final UserMapper userMapper;



    @Override
    @Transactional
    public SubscriptionDto save(User followerUser, User targetUser) {
        if (followerUser == null){
            throw new IllegalArgumentException ("followerUser is null in method save SubscriptionService");
        }
        if (targetUser == null){
            throw new IllegalArgumentException ("targetUser is null in method save SubscriptionService");
        }

        Subscription subscription = new Subscription();
        subscription.setFollowerUser(followerUser);
        subscription.setTargetUser(targetUser);

        subscriptionRepository.save(subscription);

        return subscriptionMapper.toDto(subscription);
    }

    @Override
    @Transactional
    public SubscriptionDto delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException ("followerEmail for follow user is null");
        }
        Subscription subscription = subscriptionRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subscription with id " + id + " not found"));
        subscriptionRepository.delete(subscription);
        return subscriptionMapper.toDto(subscription);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findByTargetUserEmail(String email) {
        if (email == null){
            throw new IllegalArgumentException ("email is null in findByTargetUserEmail in subscription service");
        }
        List<Subscription> subscriptions = subscriptionRepository.findByTargetUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("subscriptions not found by email " + email));

        List<User> users = subscriptions.stream()
                .map(Subscription::getFollowerUser)
                .toList();

        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public void deleteByFollowerAndTargetEmails(String senderUserEmail, String userToDeleteEmail) {
        if (senderUserEmail == null){
            throw new IllegalArgumentException ("senderUserEmail is null in method - deleteByFollowerAndTargetEmails");
        }
        subscriptionRepository.deleteByFollowerUserEmailOrTargetUserEmail(senderUserEmail,userToDeleteEmail);
    }
}
