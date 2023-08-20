package com.example.socialmediaapi.service;

public interface FriendRequestService {

    void sendFriendRequest(String emailSender, String emailReceiver);

    void acceptFriendRequest(Long id);

    void changeFriendStatus(String userEmail, Long requestId);

    void rejectFriendRequest(Long id);

}
