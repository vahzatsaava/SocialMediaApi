package com.example.socialmediaapi.repository;

import com.example.socialmediaapi.model.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    boolean existsFriendRequestBySenderUserEmailAndReceiverUserEmail(String emailSender,String emailReceiver);

    void deleteBySenderUserEmailOrReceiverUserEmail(String senderUserEmail, String userToDeleteEmail);

    Optional<FriendRequest> findBySenderUserEmailAndReceiverUserEmail(String senderUserEmail, String userToDeleteEmail);

}
