package com.example.socialmediaapi.service;



public interface FriendRequestService {

    void sendFriendRequestAndSubscribe(String emailSender, String emailReceiver);

    void acceptFriendRequest(Long id);

    void deleteFromFriends(String senderUserEmail, String userToDeleteEmail);

    void rejectFriendRequest(Long id);

    Boolean existFriendRequestByStatus(String senderEmail, String receiverEmail);

}
