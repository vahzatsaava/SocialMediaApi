package com.example.socialmediaapi.service;

import com.example.socialmediaapi.dto.SubscriptionDto;
import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.model.User;

import java.util.List;

public interface SubscriptionService {
    SubscriptionDto save(User followerUser, User targetUser);
    SubscriptionDto delete(Long id);
    List<UserDto> findByTargetUserEmail(String email);
    void  deleteByFollowerAndTargetEmails(String senderUserEmail, String userToDeleteEmail);

}
