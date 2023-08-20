package com.example.socialmediaapi.mapper;

import com.example.socialmediaapi.dto.FriendShipDto;
import com.example.socialmediaapi.model.FriendShip;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring")
public interface FriendShipMapper {

    FriendShipDto toFriendShipDto(FriendShip friendships);

    @InheritInverseConfiguration
    FriendShip toFriendShipEntity(FriendShipDto friendShip);
}
