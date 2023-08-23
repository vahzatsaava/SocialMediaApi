package com.example.socialmediaapi.repository;

import com.example.socialmediaapi.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    Optional<List<Subscription>> findByTargetUserEmail(String targetUserEmail);

    void deleteByFollowerUserEmailOrTargetUserEmail(String followUser,String targetUser);
}
