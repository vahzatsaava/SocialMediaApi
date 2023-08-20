package com.example.socialmediaapi.model;

import com.example.socialmediaapi.model.enums.FriendRequestStatus;
import com.example.socialmediaapi.model.enums.FriendShipStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "friend_request")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    private User senderUser;

    @Enumerated(EnumType.STRING)
    private FriendShipStatus friendShipStatusSenderUser;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id")
    private User receiverUser;

    @Enumerated(EnumType.STRING)
    private FriendShipStatus friendShipStatusReceiverUser;

    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;


}
