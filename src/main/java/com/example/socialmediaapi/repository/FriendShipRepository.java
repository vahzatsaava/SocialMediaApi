package com.example.socialmediaapi.repository;

import com.example.socialmediaapi.model.FriendShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip,Long> {
    boolean existsFriendShipByUser1EmailAndUser2Email(String emailUser1,String emailUser2);
}
