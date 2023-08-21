package com.example.socialmediaapi.repository;

import com.example.socialmediaapi.model.Message;
import com.example.socialmediaapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {

    @Query("SELECT m FROM Message m " +
            "WHERE (m.senderUser = :user1 AND m.receiverUser = :user2) " +
            "   OR (m.senderUser = :user2 AND m.receiverUser = :user1) " +
            "ORDER BY m.sentAt DESC")
    List<Message> findChatMessages(@Param("user1") User user1, @Param("user2") User user2);}

