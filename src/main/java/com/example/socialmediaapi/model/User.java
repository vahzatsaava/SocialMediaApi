package com.example.socialmediaapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users_media")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Post> posts;

    @OneToMany(mappedBy = "senderUser", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<FriendRequest> sentFriendRequests;

    @OneToMany(mappedBy = "receiverUser", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<FriendRequest> receivedFriendRequests;

    @ManyToMany
    @JoinTable(
            name = "friendships",
            joinColumns = @JoinColumn(name = "user1_id"),
            inverseJoinColumns = @JoinColumn(name = "user2_id")
    )
    @ToString.Exclude
    private List<User> friends;


    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")

    )
    @ToString.Exclude
    private List<Role> roles;

    @OneToMany(mappedBy = "followerUser", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Subscription> subscriptions;
}
