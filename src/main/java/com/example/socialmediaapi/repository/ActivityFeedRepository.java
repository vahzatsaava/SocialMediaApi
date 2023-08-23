package com.example.socialmediaapi.repository;

import com.example.socialmediaapi.model.ActivityFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityFeedRepository extends JpaRepository<ActivityFeed, Long> {

    List<ActivityFeed> findByUserEmailOrderByPostTimestampAsc(String userEmail);
    List<ActivityFeed> findByUserEmailOrderByPostTimestampDesc(String userEmail);
    List<ActivityFeed> findByPostId(Long postId);
    List<ActivityFeed> deleteAllByUserId(Long userId);
}
