package com.example.socialmediaapi.service;


import java.security.Principal;

public interface FriendRequestService {

    void sendFriendRequestAndSubscribe(Principal principal, String emailReceiver);

    void acceptFriendRequest(Long id);

    void deleteFromFriends(Principal principal, String userToDeleteEmail);

    void rejectFriendRequest(Long id);

    Boolean existFriendRequestByStatus(String senderEmail, String receiverEmail);

}
