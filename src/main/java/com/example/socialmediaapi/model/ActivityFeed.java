package com.example.socialmediaapi.model;

import com.example.socialmediaapi.model.enums.ActivityReadStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_feed")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityFeed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    private ActivityReadStatus activityReadStatus;

    private LocalDateTime statusChanged;
}
