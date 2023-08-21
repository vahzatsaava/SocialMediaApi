package com.example.socialmediaapi.service;

import com.example.socialmediaapi.dto.FriendShipDto;

public interface FriendShipService {
    FriendShipDto save(FriendShipDto friendship);

    void delete(Long id);

    Boolean isFriends(String emailUser1,String emailUser2);
}
