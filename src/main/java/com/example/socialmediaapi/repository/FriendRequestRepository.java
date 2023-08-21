package com.example.socialmediaapi.repository;

import com.example.socialmediaapi.model.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    boolean existsFriendRequestBySenderUserEmailAndReceiverUserEmail(String emailSender,String emailReceiver);
}
