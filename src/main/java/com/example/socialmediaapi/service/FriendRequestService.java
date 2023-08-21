package com.example.socialmediaapi.service;


public interface FriendRequestService {

    void sendFriendRequest(String emailSender, String emailReceiver);

    void acceptFriendRequest(Long id);

    void changeFriendStatus(Long subscriptionId, Long requestId,Long friendShipId);

    void rejectFriendRequest(Long id);

    Boolean existFriendRequestByStatus(String senderEmail, String receiverEmail);

}
