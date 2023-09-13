package com.example.socialmediaapi.service;


import java.security.Principal;

public interface FriendRequestService {

    void sendFriendRequestAndSubscribe(Principal principal, String emailReceiver);

    void acceptFriendRequest(String senderRequestUser, Principal principal);

    void deleteFromFriends(Principal principal, String userToDeleteEmail);

    void rejectFriendRequest(String senderRequestUser, Principal principal);

    Boolean existFriendRequestByStatus(String senderEmail, String receiverEmail);

}
