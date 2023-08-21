package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.SubscriptionDto;
import com.example.socialmediaapi.mapper.SubscriptionMapper;
import com.example.socialmediaapi.mapper.UserMapper;
import com.example.socialmediaapi.model.Subscription;
import com.example.socialmediaapi.model.User;
import com.example.socialmediaapi.repository.SubscriptionRepository;
import com.example.socialmediaapi.service.SubscriptionService;
import com.example.socialmediaapi.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final UserService userService;
    private final SubscriptionRepository subscriptionRepository;

    private final SubscriptionMapper subscriptionMapper;
    private final UserMapper userMapper;


    @Override
    @Transactional
    public SubscriptionDto save(User followerUser, User targetUser) {
        Subscription subscription = new Subscription();
        subscription.setFollowerUser(followerUser);
        subscription.setTargetUser(targetUser);

        subscriptionRepository.save(subscription);

        return subscriptionMapper.toDto(subscription);
    }

    @Override
    public SubscriptionDto delete(Long id) {
        if (id == null) {
            throw new NullPointerException("followerEmail for follow user is null");
        }
        Subscription subscription = subscriptionRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subscription with id " + id + " not found"));
        subscriptionRepository.delete(subscription);
        return subscriptionMapper.toDto(subscription);
    }

}
