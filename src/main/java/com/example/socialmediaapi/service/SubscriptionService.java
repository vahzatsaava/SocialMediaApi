package com.example.socialmediaapi.service;

import com.example.socialmediaapi.dto.SubscriptionDto;
import com.example.socialmediaapi.model.User;

public interface SubscriptionService {
    SubscriptionDto save(User followerUser, User targetUser);
    SubscriptionDto delete(Long id);
}
