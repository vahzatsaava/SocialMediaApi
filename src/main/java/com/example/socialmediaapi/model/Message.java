package com.example.socialmediaapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    private User senderUser;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id")
    private User receiverUser;

    private String content;

    private LocalDateTime sentAt;
}
